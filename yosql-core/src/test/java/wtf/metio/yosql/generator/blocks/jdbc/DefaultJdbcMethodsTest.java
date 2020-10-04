/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.tests.ObjectMother;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("DefaultJdbcMethods")
class DefaultJdbcMethodsTest extends ObjectMother {

    private DefaultJdbcMethods generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcMethods(
                yoSqlComponent().jdbcDataSourceMethods(),
                yoSqlComponent().jdbcConnectionMethods(),
                yoSqlComponent().jdbcResultSetMethods(),
                yoSqlComponent().jdbcMetaDataMethods(),
                yoSqlComponent().jdbcStatementMethods());
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