package com.snw.schema.exporter.runner;

import com.agapsys.mvn.scanner.parser.ClassInfo;
import com.snw.schema.exporter.SourceScanner;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClassLoader {

    List<String> allClasses = new ArrayList<>();
    private String targetPackagePath;
    private static ClassLoader SINGLETON = new ClassLoader();

    public static ClassLoader getInstance() {
        return SINGLETON;
    }

    private java.lang.ClassLoader getClassLoader(MavenProject project) {

        try {
            List classpathElements = project.getCompileClasspathElements();
            classpathElements.add( project.getBuild().getOutputDirectory() );
            classpathElements.add( project.getBuild().getTestOutputDirectory() );
            URL urls[] = new URL[classpathElements.size()];
            for ( int i = 0; i < classpathElements.size(); ++i) {
                File directory = new File((String) classpathElements.get(i));
                if (classpathElements.get(i).toString().contains("target")) {
                    targetPackagePath =  classpathElements.get(i).toString();
                }
                urls[i] = directory.toURL();
            }
            return new URLClassLoader( urls, this.getClass().getClassLoader() );
        } catch ( Exception e ) {
            return this.getClass().getClassLoader();
        }
    }

    public Class<?> getMainClass(MavenProject project) throws MojoExecutionException {
        try {
            ClassInfo classInfo = SourceScanner.getInstance()
                    .getFilteredClasses(new File("src/main/java")).stream()
                    .findFirst().orElseThrow(IllegalStateException::new);
            java.lang.ClassLoader classLoader = getClassLoader(project);
            allClasses = getClassNamesInPackage("C:\\Snowman\\spring-jpa-baseline\\target");
            for (String className : allClasses) {
                if (classInfo.className.equals(className))
                    continue;
                classLoader.loadClass(className);
            }
            return classLoader.loadClass(classInfo.className);
        } catch (Exception e) {
            throw new MojoExecutionException("Main class not found!");
        }
    }

    public static List<String> getClassNamesInPackage(String packageName) throws ClassNotFoundException {
        List<String> classes = new ArrayList<>();

        File[] innerFiles = new File(packageName).listFiles();
        if (innerFiles == null)
            return classes;
        for (File file : innerFiles) {
            if (file.isDirectory()) {
                String subPackage = packageName + "\\" + file.getName();
                classes.addAll(getClassNamesInPackage(subPackage));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName.substring(packageName.indexOf("target\\classes") + 15)
                        .replace("\\", ".")
                        + "." + file.getName().substring(0, file.getName().length() - 6);
                classes.add(className);
            }
        }

        return classes;
    }
}
