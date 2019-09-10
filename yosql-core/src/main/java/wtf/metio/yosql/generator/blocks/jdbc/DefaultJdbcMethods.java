package wtf.metio.yosql.generator.blocks.jdbc;

final class DefaultJdbcMethods implements JdbcMethods {

    private final JdbcDataSourceMethods dataSource;
    private final JdbcConnectionMethods connection;
    private final JdbcResultSetMethods resultSet;
    private final JdbcMetaDataMethods metaData;
    private final JdbcStatementMethods statement;

    DefaultJdbcMethods(
            final JdbcDataSourceMethods dataSource,
            final JdbcConnectionMethods connection,
            final JdbcResultSetMethods resultSet,
            final JdbcMetaDataMethods metaData,
            final JdbcStatementMethods statement) {
        this.dataSource = dataSource;
        this.connection = connection;
        this.resultSet = resultSet;
        this.metaData = metaData;
        this.statement = statement;
    }

    @Override
    public JdbcDataSourceMethods dataSource() {
        return dataSource;
    }

    @Override
    public JdbcConnectionMethods connection() {
        return connection;
    }

    @Override
    public JdbcResultSetMethods resultSet() {
        return resultSet;
    }

    @Override
    public JdbcMetaDataMethods metaData() {
        return metaData;
    }

    @Override
    public JdbcStatementMethods statement() {
        return statement;
    }

}
