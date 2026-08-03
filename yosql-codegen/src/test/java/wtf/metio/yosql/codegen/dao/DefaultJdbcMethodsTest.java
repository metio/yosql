/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("DefaultJdbcMethods")
class DefaultJdbcMethodsTest {

    private DefaultJdbcMethods generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcMethods(
                new DefaultJdbcDataSourceMethods(),
                new DefaultJdbcConnectionMethods(),
                new DefaultJdbcDatabaseMetaDataMethods(),
                new DefaultJdbcResultSetMethods(),
                new DefaultJdbcResultSetMetaDataMethods(),
                new DefaultJdbcStatementMethods());
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
        assertNotNull(generator.resultSetMetaData());
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
