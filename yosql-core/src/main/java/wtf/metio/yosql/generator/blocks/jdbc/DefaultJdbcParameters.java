package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.generator.blocks.api.Parameters;
import wtf.metio.yosql.generator.helpers.TypicalTypes;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

final class DefaultJdbcParameters implements JdbcParameters {

    private final Parameters parameters;
    private final JdbcNamesConfiguration names;

    DefaultJdbcParameters(final Parameters parameters, final JdbcNamesConfiguration names) {
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
    public ParameterSpec metaData() {
        return parameters.parameter(ResultSetMetaData.class, names.metaData());
    }

    @Override
    public ParameterSpec columnCount() {
        return parameters.parameter(TypeName.INT, names.columnCount());
    }

    @Override
    public ParameterSpec index() {
        return parameters.parameter(TypeName.INT, names.index());
    }

    @Override
    public ParameterSpec columnLabel() {
        return parameters.parameter(TypicalTypes.STRING, names.columnLabel());
    }

}
