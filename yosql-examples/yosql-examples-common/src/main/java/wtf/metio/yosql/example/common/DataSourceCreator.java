/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.example.common;

import com.zaxxer.hikari.HikariDataSource;

public final class DataSourceCreator {

    public static HikariDataSource createDataSource() {
        final var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:example;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        return dataSource;
    }

    private DataSourceCreator() {
        // factory class
    }

}
