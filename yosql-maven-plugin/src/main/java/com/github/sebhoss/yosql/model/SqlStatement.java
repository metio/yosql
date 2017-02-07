package com.github.sebhoss.yosql.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SqlStatement {

    public static final Collector<SqlStatement, ?, Map<String, List<SqlStatement>>> groupByName() {
        return Collectors.groupingBy(statement -> statement.getConfiguration().getName());
    }

    private final SqlStatementConfiguration configuration;
    private final String                    statement;

    public SqlStatement(final SqlStatementConfiguration configuration, final String statement) {
        this.configuration = configuration;
        this.statement = statement;
    }

    public SqlStatementConfiguration getConfiguration() {
        return configuration;
    }

    public String getStatement() {
        return statement;
    }

    public String getRepository() {
        return configuration.getRepository();
    }

    public boolean isReading() {
        return SqlStatementType.READING == configuration.getType();
    }

    public boolean isWriting() {
        return SqlStatementType.WRITING == configuration.getType();
    }

    public boolean shouldGenerateRxJavaAPI() {
        return configuration.isMethodRxJavaApi()
                && isReading();
    }

    public boolean shouldGenerateStandardReadAPI() {
        return configuration.isMethodStandardApi()
                && isReading();
    }

    public boolean shouldGenerateStandardWriteAPI() {
        return configuration.isMethodStandardApi()
                && isWriting();
    }

    public boolean shouldGenerateStreamEagerAPI() {
        return configuration.isMethodEagerStreamApi()
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
