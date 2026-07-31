/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;

@DisplayName("DefaultJdbcConnectionMethods")
class DefaultJdbcConnectionMethodsTest {

    private DefaultJdbcConnectionMethods generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcConnectionMethods(NamesConfigurations.defaults());
    }

    @Test
    void prepareStatement() {
        Assertions.assertEquals("""
                connection.prepareStatement(query)""", generator.prepareStatement().toString());
    }

    @Test
    void prepareCallable() {
        Assertions.assertEquals("""
                connection.prepareCall(query)""", generator.prepareCall().toString());
    }

    @Test
    void getMetaData() {
        Assertions.assertEquals("""
                connection.getMetaData()""", generator.getMetaData().toString());
    }

}
