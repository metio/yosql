/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.codegen.blocks.Parameters;
import wtf.metio.yosql.codegen.exceptions.MissingConverterAliasException;
import wtf.metio.yosql.codegen.exceptions.MissingConverterTypeNameException;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.NamesConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;

public final class DefaultJdbcParameters implements JdbcParameters {

    private final Parameters parameters;
    private final NamesConfiguration names;

    public DefaultJdbcParameters(final Parameters parameters, final NamesConfiguration names) {
        this.parameters = parameters;
        this.names = names;
    }

    @Override
    public ParameterSpec dataSource() {
        return parameters.parameter(DataSource.class, names.dataSource());
    }

    @Override
    public ParameterSpec connection() {
        return parameters.parameter(Connection.class, names.connection());
    }

    @Override
    public ParameterSpec preparedStatement() {
        return parameters.parameter(PreparedStatement.class, names.statement());
    }

    @Override
    public ParameterSpec resultSet() {
        return parameters.parameter(ResultSet.class, names.resultSet());
    }

    @Override
    public ParameterSpec resultSetMetaData() {
        return parameters.parameter(ResultSetMetaData.class, names.resultSetMetaData());
    }

    @Override
    public ParameterSpec columnCount() {
        return parameters.parameter(TypeName.INT, names.columnCount());
    }

    @Override
    public ParameterSpec index() {
        return parameters.parameter(TypeName.INT, names.indexVariable());
    }

    @Override
    public ParameterSpec columnLabel() {
        return parameters.parameter(String.class, names.columnLabel());
    }

    @Override
    public ParameterSpec converter(final ResultRowConverter converter) {
        return parameters.parameter(converter.converterTypeName().orElseThrow(MissingConverterTypeNameException::new),
                converter.alias().orElseThrow(MissingConverterAliasException::new));
    }

    @Override
    public Iterable<ParameterSpec> toMapConverterParameterSpecs() {
        return List.of(resultSet());
    }

}
