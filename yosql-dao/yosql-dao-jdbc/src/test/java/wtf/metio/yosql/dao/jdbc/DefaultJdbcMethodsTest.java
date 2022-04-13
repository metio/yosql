/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testing.codegen.Blocks;
import wtf.metio.yosql.testing.configs.Jdbc;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("DefaultJdbcMethods")
class DefaultJdbcMethodsTest {

    private DefaultJdbcMethods generator;

    @BeforeEach
    void setUp() {
        final var config = Jdbc.defaults();
        generator = new DefaultJdbcMethods(
                new DefaultJdbcDataSourceMethods(config),
                new DefaultJdbcConnectionMethods(Blocks.names(), config),
                new DefaultJdbcResultSetMethods(config),
                new DefaultJdbcMetaDataMethods(config),
                new DefaultJdbcStatementMethods(config));
    }

    @Test
    void dataSource() {
        assertNotNull(generator.dataSource());
    }

    @Test
    void connection() {
        assertNotNull(generator.connection());
    }

    @Test
    void metaData() {
        assertNotNull(generator.metaData());
    }

    @Test
    void resultSet() {
        assertNotNull(generator.resultSet());
    }

    @Test
    void statement() {
        assertNotNull(generator.statement());
    }

}