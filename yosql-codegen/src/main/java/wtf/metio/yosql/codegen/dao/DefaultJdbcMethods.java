/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.dao;

/**
 * Default implementation for JDBC methods that delegates all its work to the type-specific implementations.
 */
public record DefaultJdbcMethods(JdbcDataSourceMethods dataSource, JdbcConnectionMethods connection,
                                 JdbcDatabaseMetaDataMethods databaseMetaData, JdbcResultSetMethods resultSet,
                                 JdbcResultSetMetaDataMethods resultSetMetaData,
                                 JdbcStatementMethods statement) implements JdbcMethods {

}
