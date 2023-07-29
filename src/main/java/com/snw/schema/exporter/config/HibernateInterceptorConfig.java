package com.snw.schema.exporter.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateInterceptorConfig {

    @Bean
    public SqlStatementInspector sqlStatementInspector() {
        return new SqlStatementInspector();
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(SqlStatementInspector sqlStatementInspector) {
        return hibernateProperties -> hibernateProperties.put("hibernate.session_factory.statement_inspector", sqlStatementInspector);
    }
}
