package com.github.sebhoss.yosql.generator;

import com.github.sebhoss.yosql.SqlStatementConfiguration;

public class TypicalFields {

    public static final String constantSqlStatementFieldName(final SqlStatementConfiguration configuration) {
        return configuration.getName().replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
    }

    public static final String constantRawSqlStatementFieldName(final SqlStatementConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + "_RAW";
    }

    public static final String constantSqlStatementParametersFieldName(final SqlStatementConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + "_PARAMETER_INDEX";
    }

}
