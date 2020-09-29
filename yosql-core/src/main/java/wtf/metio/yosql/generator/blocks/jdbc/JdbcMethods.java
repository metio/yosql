/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;

/**
 * Entry point for methods of the JDBC API. Nested interfaces are used to distinguish the source of the methods.
 */
public interface JdbcMethods {

    JdbcDataSourceMethods dataSource();

    JdbcConnectionMethods connection();

    JdbcResultSetMethods resultSet();

    JdbcMetaDataMethods metaData();

    JdbcStatementMethods statement();

    /**
     * Methods that are exposed by a JDBC DataSource object.
     *
     * @see javax.sql.DataSource
     */
    interface JdbcDataSourceMethods {
        CodeBlock getConnection();
    }

    /**
     * Methods that are exposed by a JDBC Connection object.
     *
     * @see java.sql.Connection
     */
    interface JdbcConnectionMethods {
        CodeBlock prepareStatement();

        CodeBlock prepareCallable();
    }

    /**
     * Methods that are exposed by a JDBC ResultSet object.
     *
     * @see java.sql.ResultSet
     */
    interface JdbcResultSetMethods {
        CodeBlock getMetaData();
    }

    /**
     * Methods that are exposed by a JDBC ResultSetMetaData object.
     *
     * @see java.sql.ResultSetMetaData
     */
    interface JdbcMetaDataMethods {
        CodeBlock getColumnCount();
    }

    /**
     * Methods that are exposed by a JDBC Statement object.
     *
     * @see java.sql.Statement
     * @see java.sql.PreparedStatement
     */
    interface JdbcStatementMethods {
        CodeBlock executeQuery();

        CodeBlock executeUpdate();

        CodeBlock executeBatch();

        CodeBlock addBatch();
    }

}
