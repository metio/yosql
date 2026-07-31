/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.example.common;

import com.zaxxer.hikari.HikariDataSource;

public final class DataSourceCreator {

    public static HikariDataSource createDataSource(final String name) {
        final var dataSource = new HikariDataSource();
        dataSource.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
        dataSource.addDataSourceProperty("URL", "jdbc:h2:mem:" + name);
        dataSource.addDataSourceProperty("user", "sa");
        return dataSource;
    }

    private DataSourceCreator() {
        // factory class
    }

}
