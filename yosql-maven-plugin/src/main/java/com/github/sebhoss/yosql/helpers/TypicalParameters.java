package com.github.sebhoss.yosql.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Arrays;

import javax.sql.DataSource;

import com.github.sebhoss.yosql.SqlFileParser;
import com.github.sebhoss.yosql.SqlParameter;
import com.github.sebhoss.yosql.SqlStatement;
import com.squareup.javapoet.ArrayTypeName;
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

    public static ParameterSpec columnLabel() {
        return parameter(TypicalTypes.STRING, TypicalNames.COLUMN_LABEL);
    }

    public static Iterable<ParameterSpec> resultState(final TypeName type) {
        return Arrays.asList(parameter(type, TypicalNames.RESULT));
    }

    public static ParameterSpec parameter(final Class<?> type, final String name) {
        return parameter(ClassName.get(type), name);
    }

    public static ParameterSpec parameter(final TypeName type, final String name) {
        return ParameterSpec.builder(type, name, TypicalModifiers.PARAMETER).build();
    }

    public static ParameterSpec ofSqlParameter(final SqlParameter parameter) {
        return parameter(guessTypeName(parameter.getType()), parameter.getName());
    }

    public static ParameterSpec batchOfSqlParameter(final SqlParameter parameter) {
        return parameter(guessBatchTypeName(parameter.getType()), parameter.getName());
    }

    private static TypeName guessBatchTypeName(final String type) {
        return ArrayTypeName.of(guessTypeName(type));
    }

    private static TypeName guessTypeName(final String type) {
        if (type.contains(".")) {
            return ClassName.bestGuess(type);
        } else {
            switch (type) {
            case "boolean":
                return TypeName.BOOLEAN;
            case "byte":
                return TypeName.BYTE;
            case "short":
                return TypeName.SHORT;
            case "long":
                return TypeName.LONG;
            case "char":
                return TypeName.CHAR;
            case "float":
                return TypeName.FLOAT;
            case "double":
                return TypeName.DOUBLE;
            case "int":
                return TypeName.INT;
            default:
                return TypeName.OBJECT;
            }
        }
    }

    public static final String replaceNamedParameters(final SqlStatement sqlStatement) {
        return sqlStatement.getStatement().replaceAll(SqlFileParser.PATTERN.pattern(), "?");
    }

}
