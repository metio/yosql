package com.github.sebhoss.yosql.helpers;

import com.github.sebhoss.yosql.model.SqlStatementConfiguration;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

public class TypicalFields {

    public static final FieldSpec privateField(final TypeName type, final String name) {
        return FieldSpec.builder(type, name)
                .addModifiers(TypicalModifiers.PRIVATE_FIELD)
                .build();
    }

    public static final String constantSqlStatementFieldName(final SqlStatementConfiguration configuration) {
        return configuration.getName().replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase() + getVendor(configuration);
    }

    public static final String constantRawSqlStatementFieldName(final SqlStatementConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + "_RAW" + getVendor(configuration);
    }

    public static final String constantSqlStatementParameterIndexFieldName(
            final SqlStatementConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + "_PARAMETER_INDEX";
    }

    private static final String getVendor(final SqlStatementConfiguration configuration) {
        return configuration.getVendor() == null ? "" : "_" + configuration.getVendor().replace(" ", "_").toUpperCase();
    }

}
