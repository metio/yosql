/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
