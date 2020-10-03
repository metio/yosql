/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

/**
 * Commonly used names for generated code that uses the JDBC API.
 */
public interface JdbcNames {

    /**
     * @return The name for a DataSource variable.
     */
    String dataSource();

    /**
     * @return The name for a Connection variable.
     */
    String connection();

    /**
     * @return The name for a Statement variable.
     */
    String statement();

    /**
     * @return The name for a MetaData variable.
     */
    String metaData();

    /**
     * @return The name for a ResultSet variable.
     */
    String resultSet();

    /**
     * @return The name for a ColumnCount variable.
     */
    String columnCount();

    /**
     * @return The name for a ColumnLabel variable.
     */
    String columnLabel();

    /**
     * @return The name for a Batch variable.
     */
    String batch();

    /**
     * @return The name for a List variable.
     */
    String list();

    /**
     * @return The name for a JDBC Index variable.
     */
    String jdbcIndex();

    /**
     * @return The name for a Index variable.
     */
    String index();

    /**
     * @return The name for a Row variable.
     */
    String row();

}
