/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import wtf.metio.yosql.model.configuration.JdbcFieldsConfiguration;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;
import static wtf.metio.yosql.model.configuration.ModelConfigurationObjectMother.jdbcNamesConfiguration;

public final class JdbcObjectMother {

    public static JdbcFieldsConfiguration jdbcFieldsConfiguration() {
        return JdbcFieldsConfiguration.builder()
                .setIndexSuffix("_INDEX")
                .setRawSuffix("_RAW")
                .build();
    }

    public static JdbcMethods.JdbcConnectionMethods jdbcConnectionMethods() {
        return new DefaultJdbcConnectionMethods(names(), jdbcNames());
    }

    public static JdbcMethods.JdbcDataSourceMethods jdbcDataSourceMethods() {
        return new DefaultJdbcDataSourceMethods(jdbcNames());
    }

    public static JdbcMethods.JdbcResultSetMethods jdbcResultSetMethods() {
        return new DefaultJdbcResultSetMethods(jdbcNames());
    }

    public static JdbcMethods.JdbcMetaDataMethods jdbcMetaDataMethods() {
        return new DefaultJdbcMetaDataMethods(jdbcNames());
    }

    public static JdbcMethods.JdbcStatementMethods jdbcStatementMethods() {
        return new DefaultJdbcStatementMethods(jdbcNames());
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
        return new DefaultJdbcNames(jdbcNamesConfiguration());
    }

    private JdbcObjectMother() {
        // factory class
    }

}
