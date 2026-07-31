/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;

@DisplayName("DefaultJdbcDataSourceMethods")
class DefaultJdbcDataSourceMethodsTest {

    private DefaultJdbcDataSourceMethods generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcDataSourceMethods(NamesConfigurations.defaults());
    }

    @Test
    void getConnection() {
        Assertions.assertEquals("""
                dataSource.getConnection()""", generator.getConnection().toString());
    }

}
