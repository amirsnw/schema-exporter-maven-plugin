package com.snw.schema.exporter.config;

import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SqlStatementInspector implements StatementInspector {

    private static final Pattern SQL_COMMENT_PATTERN = Pattern
            .compile(
                    "\\/\\*.*?\\*\\/\\s*"
            );

    private List<String> sqlQueries = new ArrayList<>();

    public List<String> getSqlQueries() {
        return sqlQueries;
    }

    @Override
    public String inspect(String sql) {
        /*SQL_COMMENT_PATTERN
                .matcher(sql)
                .replaceAll("");*/
        sqlQueries.add(sql);
        return sql;
    }
}