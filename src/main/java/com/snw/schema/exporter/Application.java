package com.snw.schema.exporter;

import com.snw.schema.exporter.entity.StudentEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
	
    public static ConfigurableApplicationContext runner(Class clazz) {
        ConfigurableApplicationContext context = SpringApplication.run(clazz, "");
        return context;
    }

    public static void main(String[] args) {
        ApplicationContext context = runner(Application.class);
    }
}
