/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("DefaultJdbcMethods")
class DefaultJdbcMethodsTest {

    private DefaultJdbcMethods generator;

    @BeforeEach
    void setUp() {
        final var names = NamesConfigurations.defaults();
        generator = new DefaultJdbcMethods(
                new DefaultJdbcDataSourceMethods(names),
                new DefaultJdbcConnectionMethods(names),
                new DefaultJdbcDatabaseMetaDataMethods(names),
                new DefaultJdbcResultSetMethods(names),
                new DefaultJdbcResultSetMetaDataMethods(names),
                new DefaultJdbcStatementMethods(names));
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
