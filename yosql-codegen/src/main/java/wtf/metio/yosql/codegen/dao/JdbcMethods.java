/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.CodeBlock;

/**
 * Entry point for methods of the JDBC API. Nested interfaces are used to distinguish the source of the methods.
 */
public interface JdbcMethods {

    JdbcDataSourceMethods dataSource();

    JdbcConnectionMethods connection();

    JdbcDatabaseMetaDataMethods databaseMetaData();

    JdbcResultSetMethods resultSet();

    JdbcResultSetMetaDataMethods resultSetMetaData();

    JdbcStatementMethods statement();

    /**
     * Methods that are exposed by a JDBC DataSource object.
     *
     * @see javax.sql.DataSource
     */
    interface JdbcDataSourceMethods {

        /**
         * @see javax.sql.DataSource#getConnection()
         */
        CodeBlock getConnection();

    }

    /**
     * Methods that are exposed by a JDBC Connection object.
     *
     * @see java.sql.Connection
     */
    interface JdbcConnectionMethods {

        /**
         * @see java.sql.Connection#createStatement()
         */
        CodeBlock createStatement();

        /**
         * @see java.sql.Connection#prepareStatement(String)
         */
        CodeBlock prepareStatement();

        /**
         * @see java.sql.Connection#prepareCall(String)
         */
        CodeBlock prepareCall();

        /**
         * @see java.sql.Connection#getMetaData()
         */
        CodeBlock getMetaData();

    }

    /**
     * @see java.sql.DatabaseMetaData
     */
    interface JdbcDatabaseMetaDataMethods {

        /**
         * @see java.sql.DatabaseMetaData#getDatabaseProductName()
         */
        CodeBlock getDatabaseProductName();

    }

    /**
     * Methods that are exposed by a JDBC ResultSet object.
     *
     * @see java.sql.ResultSet
     */
    interface JdbcResultSetMethods {

        /**
         * @see java.sql.ResultSet#getMetaData()
         */
        CodeBlock getMetaData();

    }

    /**
     * Methods that are exposed by a JDBC ResultSetMetaData object.
     *
     * @see java.sql.ResultSetMetaData
     */
    interface JdbcResultSetMetaDataMethods {

        /**
         * @see java.sql.ResultSetMetaData#getColumnCount()
         */
        CodeBlock getColumnCount();

    }

    /**
     * Methods that are exposed by a JDBC Statement object.
     *
     * @see java.sql.Statement
     * @see java.sql.PreparedStatement
     */
    interface JdbcStatementMethods {

        /**
         * @see java.sql.PreparedStatement#executeQuery()
         */
        CodeBlock executeQuery();

        /**
         * @see java.sql.Statement#executeQuery(String)
         */
        CodeBlock executeGivenQuery();

        /**
         * @see java.sql.Statement#executeUpdate(String)
         */
        CodeBlock executeUpdate();

        /**
         * @see java.sql.Statement#executeBatch()
         */
        CodeBlock executeBatch();

        /**
         * @see java.sql.Statement#addBatch(String)
         */
        CodeBlock addBatch();

        /**
         * @see java.sql.Statement#getResultSet()
         */
        CodeBlock getResultSet();

        /**
         * @see java.sql.Statement#execute(String)
         */
        CodeBlock execute();

    }

}
