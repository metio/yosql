/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.sql.ResultRowConverter;

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
        return parameters.parameter(TypicalTypes.STRING, names.columnLabel());
    }

    @Override
    public ParameterSpec converter(final ResultRowConverter converter) {
        return parameters.parameter(ClassName.bestGuess(converter.converterType()), converter.alias());
    }

    @Override
    public Iterable<ParameterSpec> toMapConverterParameterSpecs() {
        return List.of(resultSet());
    }

}
