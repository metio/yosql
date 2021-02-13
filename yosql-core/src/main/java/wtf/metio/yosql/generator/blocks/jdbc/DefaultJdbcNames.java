/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import wtf.metio.yosql.model.configuration.JdbcConfiguration;

final class DefaultJdbcNames implements JdbcNames {

    private final JdbcConfiguration configuration;

    DefaultJdbcNames(final JdbcConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String dataSource() {
        return configuration.dataSource();
    }

    @Override
    public String connection() {
        return configuration.connection();
    }

    @Override
    public String statement() {
        return configuration.statement();
    }

    @Override
    public String metaData() {
        return configuration.metaData();
    }

    @Override
    public String resultSet() {
        return configuration.resultSet();
    }

    @Override
    public String columnCount() {
        return configuration.columnCount();
    }

    @Override
    public String columnLabel() {
        return configuration.columnLabel();
    }

    @Override
    public String batch() {
        return configuration.batch();
    }

    @Override
    public String list() {
        return configuration.list();
    }

    @Override
    public String jdbcIndex() {
        return configuration.jdbcIndex();
    }

    @Override
    public String index() {
        return configuration.index();
    }

    @Override
    public String row() {
        return configuration.row();
    }

}
