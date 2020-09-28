/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import wtf.metio.yosql.model.configuration.JdbcFieldsConfiguration;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;

public final class JdbcObjectMother {
    
    public static JdbcFieldsConfiguration jdbcFieldsConfiguration() {
        return JdbcFieldsConfiguration.builder()
                .setIndexSuffix("_INDEX")
                .setRawSuffix("_RAW")
                .build();
    }

    public static JdbcNamesConfiguration jdbcNamesConfiguration() {
        return JdbcNamesConfiguration.builder()
                .setStatement("statement")
                .setRow("row")
                .setResultSet("resultSet")
                .setMetaData("metaData")
                .setList("list")
                .setJdbcIndex("jdbcIndex")
                .setIndex("index")
                .setDataSource("dataSource")
                .setConnection("connection")
                .setColumnLabel("columnLabel")
                .setColumnCount("columnCount")
                .setBatch("batch")
                .build();
    }

    public static JdbcMethods.JdbcConnectionMethods jdbcConnectionMethods() {
        return new DefaultJdbcConnectionMethods(names(), jdbcNamesConfiguration());
    }
    
    public static JdbcMethods.JdbcDataSourceMethods jdbcDataSourceMethods() {
        return new DefaultJdbcDataSourceMethods(jdbcNamesConfiguration());
    }

    public static JdbcMethods.JdbcResultSetMethods jdbcResultSetMethods() {
        return new DefaultJdbcResultSetMethods(jdbcNamesConfiguration());
    }

    public static JdbcMethods.JdbcMetaDataMethods jdbcMetaDataMethods() {
        return new DefaultJdbcMetaDataMethods(jdbcNamesConfiguration());
    }
    
    public static JdbcMethods.JdbcStatementMethods jdbcStatementMethods() {
        return new DefaultJdbcStatementMethods(jdbcNamesConfiguration());
    }
    
    public static JdbcFields jdbcFields() {
        return new DefaultJdbcFields(jdbcFieldsConfiguration());
    }
    
    public static JdbcMethods jdbcMethods() {
        return new DefaultJdbcMethods(
                jdbcDataSourceMethods(),
                jdbcConnectionMethods(),
                jdbcResultSetMethods(),
                jdbcMetaDataMethods(),
                jdbcStatementMethods());
    }
    
    public static JdbcNames jdbcNames() {
        return new DefaultJdbcNames();
    }

    private JdbcObjectMother() {
        // factory class
    }

}
