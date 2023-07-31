package com.snw.schema.exporter;

import com.snw.schema.exporter.runner.ApplicationRunner;
import com.snw.schema.exporter.runner.ClassLoader;
import com.snw.schema.exporter.runner.ProjectModel;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name = "export", defaultPhase = LifecyclePhase.COMPILE,
        requiresDependencyResolution = ResolutionScope.COMPILE)
public class SchemaExportMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}")
    private String targetDirectory;

    @Parameter(defaultValue = "${project}")
    public MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        ProjectModel model = ClassLoader.getInstance()
                .getProjectModel(project, targetDirectory);
        ApplicationRunner.runner(model);
    }
}
