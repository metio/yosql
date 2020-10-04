/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

/**
 * Configures names used during code generation with the JDBC API.
 */
public class JdbcNames {

    private final String batch = "batch";
    private final String columnCount = "columnCount";
    private final String columnLabel = "columnLabel";
    private final String connection = "connection";
    private final String dataSource = "dataSource";
    private final String index = "index";
    private final String jdbcIndex = "jdbcIndex";
    private final String list = "list";
    private final String metaData = "metaData";
    private final String resultSet = "resultSet";
    private final String row = "row";
    private final String statement = "statement";

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
