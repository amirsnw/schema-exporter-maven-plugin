package com.snw.schema.exporter.runner;

import com.snw.schema.exporter.config.HibernateInterceptorConfig;
import com.snw.schema.exporter.config.SqlStatementInspector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;


@SpringBootApplication
public class ApplicationRunner {

    public static ConfigurableApplicationContext runner(Class clazz) {
        SpringApplication application = new SpringApplication(clazz, HibernateInterceptorConfig.class);
        // application.addInitializers(new MyInitializer()); // Alternative way of importing spring external config
        ConfigurableApplicationContext parentContext = application.run("");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setParent(parentContext);
        context.scan(clazz.getPackage().getName());
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