package com.github.sebhoss.yosql;

public class SqlStatement {

    private final SqlStatementConfiguration configuration;
    private final String statement;

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

}
