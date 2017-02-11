package com.github.sebhoss.yosql.generator.helpers;

import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

public class TypicalFields {

    public static final FieldSpec privateField(final TypeName type, final String name) {
        return FieldSpec.builder(type, name)
                .addModifiers(TypicalModifiers.PRIVATE_FIELD)
                .build();
    }

    public static final String constantSqlStatementFieldName(final SqlConfiguration configuration) {
        return configuration.getName().replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase() + getVendor(configuration);
    }

    public static final String constantRawSqlStatementFieldName(final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + "_RAW" + getVendor(configuration);
    }

    public static final String constantSqlStatementParameterIndexFieldName(
            final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + "_PARAMETER_INDEX";
    }

    private static final String getVendor(final SqlConfiguration configuration) {
        return configuration.getVendor() == null ? "" : "_" + configuration.getVendor().replace(" ", "_").toUpperCase();
    }

}
