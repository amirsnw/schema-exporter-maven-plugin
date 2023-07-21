package com.snw.schema.exporter;

import jakarta.persistence.EntityManagerFactory;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.EnumSet;

@Mojo(name = "export", defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE)
public class SchemaExportMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}/schema.sql")
    private String outputFile;

    @Parameter(defaultValue = "src/main/resources/application.yml")
    private String configFile;

    @Parameter(defaultValue = "${project}")
    public MavenProject project;

    @Override
    public void execute() throws MojoExecutionException {
        ApplicationContext context = new SpringApplicationBuilder()
                .sources(ClassLoader.getInstance().getMainClass(this.project))
                .properties("spring.config.location=" + configFile)
                .run();

        DatabaseMetaData metadata = null;
        try {
            EntityManagerFactory entityManagerFactory = context.getBean(LocalContainerEntityManagerFactoryBean.class)
                    .getObject();
            metadata = entityManagerFactory.createEntityManager().unwrap(Connection.class).getMetaData();
        } catch (SQLException e) {
            throw new MojoExecutionException("Error extracting schema!");
        }

        SchemaExport schemaExport = new SchemaExport();
        schemaExport.create(EnumSet.of(TargetType.SCRIPT), (Metadata) metadata);

        schemaExport.setOutputFile(outputFile);
        schemaExport.setDelimiter(";");

        schemaExport.setFormat(true);
        schemaExport.setHaltOnError(true);
    }
}
