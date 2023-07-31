package com.snw.schema.exporter.runner;

import com.snw.schema.exporter.config.HibernateInterceptorConfig;
import com.snw.schema.exporter.config.SqlStatementInspector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class ApplicationRunner {

    public static ConfigurableApplicationContext runner(ProjectModel model) {
        // SpringApplication application = new SpringApplication(model.getMainClass(), HibernateInterceptorConfig.class);
        Class[] allClasses = Arrays.copyOf(model.getAllClasses(), model.getAllClasses().length + 1);
        System.arraycopy(new Class[]{HibernateInterceptorConfig.class}, 0, allClasses,
                model.getAllClasses().length, 1);
        // application.addInitializers(new MyInitializer()); // Alternative way of importing spring external config
        ConfigurableApplicationContext parentContext = SpringApplication.run(allClasses, new String[]{});
        // sticks here and gives "Invalid bean definition"
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setParent(parentContext);
        context.scan(model.getMainClass().getPackage().getName());
        context.refresh();
        SqlStatementInspector sqlStatementInspector = context.getBean(SqlStatementInspector.class);
        printQueries(sqlStatementInspector.getSqlQueries());
        return context;
    }

    private static void printQueries(List<String> sqlQueries) {
        for (String sql : sqlQueries) {
            System.out.println(sql);
        }
    }
}