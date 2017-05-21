/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import de.xn__ho_hia.javapoet.TypeGuesser;
import de.xn__ho_hia.yosql.model.SqlParameter;
import de.xn__ho_hia.yosql.parser.SqlFileParser;

@SuppressWarnings({ "nls", "javadoc" })
public final class TypicalParameters {

    public static ParameterSpec dataSource() {
        return parameter(DataSource.class, TypicalNames.DATA_SOURCE);
    }

    public static ParameterSpec connection() {
        return parameter(Connection.class, TypicalNames.CONNECTION);
    }

    public static ParameterSpec preparedStatement() {
        return parameter(PreparedStatement.class, TypicalNames.STATEMENT);
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
        return ParameterSpec.builder(type, name, TypicalModifiers.parmeter()).build();
    }

    public static ParameterSpec ofSqlParameter(final SqlParameter parameter) {
        return parameter(TypeGuesser.guessTypeName(parameter.getType()), parameter.getName());
    }

    public static ParameterSpec batchOfSqlParameter(final SqlParameter parameter) {
        return parameter(ArrayTypeName.of(TypeGuesser.guessTypeName(parameter.getType())), parameter.getName());
    }

    public static final String replaceNamedParameters(final String rawSqlStatement) {
        return rawSqlStatement.replaceAll(SqlFileParser.PARAMETER_PATTERN.pattern(), "?");
    }

    public static final Iterable<ParameterSpec> asParameterSpecs(final List<SqlParameter> parameters) {
        return asParameterSpecs(parameters, TypicalParameters::ofSqlParameter);
    }

    public static final Iterable<ParameterSpec> asBatchParameterSpecs(final List<SqlParameter> parameters) {
        return asParameterSpecs(parameters, TypicalParameters::batchOfSqlParameter);
    }

    private static Iterable<ParameterSpec> asParameterSpecs(final List<SqlParameter> parameters,
            final Function<SqlParameter, ParameterSpec> asParameter) {
        return Optional.ofNullable(parameters)
                .map(params -> params.stream()
                        .map(asParameter)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

}
