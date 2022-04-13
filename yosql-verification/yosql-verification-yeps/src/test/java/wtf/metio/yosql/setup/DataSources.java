/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.setup;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSources {

    public static DataSource createDataSource(final int yep) {
        final var dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:mem:YEP-" + yep);
        dataSource.setUsername("sa");
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(1);
        return dataSource;
    }

}
