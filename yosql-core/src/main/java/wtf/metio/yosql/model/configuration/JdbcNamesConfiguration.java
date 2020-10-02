/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;

@Value.Immutable
public interface JdbcNamesConfiguration {

    static JdbcNamesConfiguration.Builder builder() {
        return new JdbcNamesConfiguration.Builder();
    }

    String dataSource();

    String connection();

    String columnCount();

    String columnLabel();

    String statement();

    String metaData();

    String resultSet();

    String batch();

    String list();

    String jdbcIndex();

    String index();

    String row();

    class Builder extends ImmutableJdbcNamesConfiguration.Builder {
    }

}
