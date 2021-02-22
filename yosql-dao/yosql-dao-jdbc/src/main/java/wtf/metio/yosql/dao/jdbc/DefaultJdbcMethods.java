/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

/**
 * Default implementation for JDBC methods that delegates all its work to the type-specific implementations.
 */
public final class DefaultJdbcMethods implements JdbcMethods {

    private final JdbcDataSourceMethods dataSource;
    private final JdbcConnectionMethods connection;
    private final JdbcResultSetMethods resultSet;
    private final JdbcMetaDataMethods metaData;
    private final JdbcStatementMethods statement;

    public DefaultJdbcMethods(
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
