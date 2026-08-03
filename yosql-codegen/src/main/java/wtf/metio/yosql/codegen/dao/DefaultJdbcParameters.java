/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.ParameterSpec;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.codegen.blocks.Parameters;
import wtf.metio.yosql.codegen.exceptions.MissingConverterAliasException;
import wtf.metio.yosql.codegen.exceptions.MissingConverterTypeNameException;
import wtf.metio.yosql.models.configuration.ResultRowConverter;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;

public final class DefaultJdbcParameters implements JdbcParameters {

    private final Parameters parameters;

    public DefaultJdbcParameters(final Parameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public ParameterSpec dataSource() {
        return parameters.parameter(DataSource.class, GeneratedNames.DATA_SOURCE);
    }

    @Override
    public ParameterSpec connection() {
        return parameters.parameter(Connection.class, GeneratedNames.CONNECTION);
    }

    @Override
    public ParameterSpec preparedStatement() {
        return parameters.parameter(PreparedStatement.class, GeneratedNames.STATEMENT);
    }

    @Override
    public ParameterSpec resultSet() {
        return parameters.parameter(ResultSet.class, GeneratedNames.RESULT_SET);
    }

    @Override
    public ParameterSpec resultSetMetaData() {
        return parameters.parameter(ResultSetMetaData.class, GeneratedNames.RESULT_SET_META_DATA);
    }

    @Override
    public ParameterSpec columnCount() {
        return parameters.parameter(TypeName.INT, GeneratedNames.COLUMN_COUNT);
    }

    @Override
    public ParameterSpec index() {
        return parameters.parameter(TypeName.INT, GeneratedNames.INDEX);
    }

    @Override
    public ParameterSpec columnLabel() {
        return parameters.parameter(String.class, GeneratedNames.COLUMN_LABEL);
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
