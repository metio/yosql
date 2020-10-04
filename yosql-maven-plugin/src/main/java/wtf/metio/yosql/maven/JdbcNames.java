/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

/**
 * Configures names used during code generation with the JDBC API.
 */
public class JdbcNames {

    @Parameter(defaultValue = "batch")
    private String batch;
    @Parameter(defaultValue = "columnCount")
    private String columnCount;
    @Parameter(defaultValue = "columnLabel")
    private String columnLabel;
    @Parameter(defaultValue = "connection")
    private String connection;
    @Parameter(defaultValue = "dataSource")
    private String dataSource;
    @Parameter(defaultValue = "index")
    private String index;
    @Parameter(defaultValue = "jdbcIndex")
    private String jdbcIndex;
    @Parameter(defaultValue = "list")
    private String list;
    @Parameter(defaultValue = "metaData")
    private String metaData;
    @Parameter(defaultValue = "resultSet")
    private String resultSet;
    @Parameter(defaultValue = "row")
    private String row;
    @Parameter(defaultValue = "statement")
    private String statement;

    JdbcNamesConfiguration asConfiguration() {
        return JdbcNamesConfiguration.builder()
                .setBatch(batch)
                .setColumnCount(columnCount)
                .setColumnLabel(columnLabel)
                .setConnection(connection)
                .setDataSource(dataSource)
                .setIndex(index)
                .setJdbcIndex(jdbcIndex)
                .setList(list)
                .setMetaData(metaData)
                .setResultSet(resultSet)
                .setRow(row)
                .setStatement(statement)
                .build();
    }

}
