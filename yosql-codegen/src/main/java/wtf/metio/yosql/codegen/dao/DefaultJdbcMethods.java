/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
