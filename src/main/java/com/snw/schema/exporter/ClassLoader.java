package com.snw.schema.exporter;

import com.agapsys.mvn.scanner.parser.ClassInfo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class ClassLoader {

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
                File file = new File( (String) classpathElements.get( i ) );
                urls[i] = file.toURL();
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
            return getClassLoader(project).loadClass(classInfo.className);
        } catch (Exception e) {
            throw new MojoExecutionException("Main class not found!");
        }
    }
}
