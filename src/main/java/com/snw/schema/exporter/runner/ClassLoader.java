package com.snw.schema.exporter.runner;

import com.agapsys.mvn.scanner.parser.ClassInfo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClassLoader {

    private static ClassLoader INSTANCE = new ClassLoader();

    public static ClassLoader getInstance() {
        return INSTANCE;
    }

    private java.lang.ClassLoader getClassLoader(MavenProject project) {

        String element;
        File directory;
        URL[] urls;
        List<String> classpathElements;
        try {
            classpathElements = project.getCompileClasspathElements();
            classpathElements.add(project.getBuild().getOutputDirectory());
            classpathElements.add(project.getBuild().getTestOutputDirectory());

            urls = new URL[classpathElements.size()];
            for (int i = 0 ; i < classpathElements.size() ; ++i) {
                element = classpathElements.get(i);
                directory = new File(element);
                urls[i] = directory.toURL();
            }
            return new URLClassLoader(urls, this.getClass().getClassLoader());
        } catch (Exception e) {
            return this.getClass().getClassLoader();
        }
    }

    public ProjectModel getProjectModel(MavenProject project, String targetDirectory)
            throws MojoExecutionException {
        try {
            ClassInfo mainClassInfo = SourceScanner.getInstance()
                    .getFilteredClasses(new File("src/main/java")).stream()
                    .findFirst().orElseThrow(IllegalStateException::new);
            java.lang.ClassLoader classLoader = getClassLoader(project);

            ProjectModel ProjectModel = new ProjectModel(classLoader.loadClass(mainClassInfo.className),
                    loadAllClasses(classLoader, targetDirectory));
            return ProjectModel;
        } catch (Exception e) {
            throw new MojoExecutionException("Main class not found!");
        }
    }

    private Class[] loadAllClasses(java.lang.ClassLoader classLoader,
                                String targetDirectory) throws ClassNotFoundException {
        List<Class> allClasses = new ArrayList<>();
        for (String className : getClassNamesInPackage(targetDirectory)) {
            allClasses.add(classLoader.loadClass(className));
        }
        return allClasses.toArray(new Class[0]);
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
