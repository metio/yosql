package com.github.sebhoss.yosql.generator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.sql.DataSource;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

public final class TypicalParameters {

    public static ParameterSpec dataSource() {
        return parameter(DataSource.class, TypicalNames.DATA_SOURCE);
    }

    public static ParameterSpec connection() {
        return parameter(Connection.class, TypicalNames.CONNECTION);
    }

    public static ParameterSpec preparedStatement() {
        return parameter(PreparedStatement.class, TypicalNames.PREPARED_STATEMENT);
    }

    public static ParameterSpec resultSet() {
        return parameter(ResultSet.class, TypicalNames.RESULT_SET);
    }

    public static ParameterSpec metaData() {
        return parameter(ResultSetMetaData.class, TypicalNames.META_DATA);
    }

    public static ParameterSpec columnCount() {
        return parameter(TypeName.INT, TypicalNames.COLUMN_COUNT);
    }

    public static ParameterSpec index() {
        return parameter(TypeName.INT, TypicalNames.INDEX);
    }

    public static ParameterSpec parameter(final Class<?> type, final String name) {
        return parameter(ClassName.get(type), name);
    }

    public static ParameterSpec parameter(final TypeName type, final String name) {
        return ParameterSpec.builder(type, name, TypicalModifiers.PARAMETER).build();
    }

}
