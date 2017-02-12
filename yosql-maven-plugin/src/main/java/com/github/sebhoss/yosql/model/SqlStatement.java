package com.github.sebhoss.yosql.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SqlStatement {

    public static final Collector<SqlStatement, ?, Map<String, List<SqlStatement>>> groupByName() {
        return Collectors.groupingBy(statement -> statement.getConfiguration().getName());
    }

    private final SqlConfiguration configuration;
    private final String           rawStatement;

    public SqlStatement(final SqlConfiguration configuration, final String rawStatement) {
        this.configuration = configuration;
        this.rawStatement = rawStatement;
    }

    public SqlConfiguration getConfiguration() {
        return configuration;
    }

    public String getName() {
        return configuration.getName();
    }

    public String getRawStatement() {
        return rawStatement;
    }

    public String getRepository() {
        return configuration.getRepository();
    }

    public boolean isReading() {
        return SqlType.READING == configuration.getType();
    }

    public boolean isWriting() {
        return SqlType.WRITING == configuration.getType();
    }

    public boolean isCalling() {
        return SqlType.CALLING == configuration.getType();
    }

    public boolean shouldGenerateRxJavaAPI() {
        return configuration.isMethodRxJavaApi()
                && isReading();
    }

    public boolean shouldGenerateStandardReadAPI() {
        return configuration.isMethodStandardApi()
                && isReading();
    }

    public boolean shouldGenerateStandardCallAPI() {
        return configuration.isMethodStandardApi()
                && isCalling();
    }

    public boolean shouldGenerateStandardWriteAPI() {
        return configuration.isMethodStandardApi()
                && isWriting();
    }

    public boolean shouldGenerateStreamEagerAPI() {
        return configuration.isMethodStreamEagerApi()
                && isReading();
    }

    public boolean shouldGenerateStreamLazyAPI() {
        return configuration.isMethodStreamLazyApi()
                && isReading();
    }

    public boolean shouldGenerateBatchAPI() {
        return configuration.isMethodBatchApi()
                && configuration.hasParameters()
                && isWriting();
    }

}
