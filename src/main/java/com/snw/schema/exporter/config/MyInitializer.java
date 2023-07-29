package com.snw.schema.exporter.config;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class MyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // Get a reference to the bean factory
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        
        // Create a new instance of your external bean
        SqlStatementInspector sqlStatementInspector = new SqlStatementInspector();
        HibernatePropertiesCustomizer hibernatePropertiesCustomizer =
                hibernateProperties -> hibernateProperties.put("hibernate.session_factory.statement_inspector", sqlStatementInspector);;

        // Add the external bean to the context
        beanFactory.registerSingleton("sqlStatementInspector", sqlStatementInspector);
        beanFactory.registerSingleton("hibernatePropertiesCustomizer", hibernatePropertiesCustomizer);
    }
}