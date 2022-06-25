/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;

@DisplayName("DefaultJdbcStatementMethods")
class DefaultJdbcStatementMethodsTest {

    private DefaultJdbcStatementMethods generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcStatementMethods(NamesConfigurations.defaults());
    }

    @Test
    void executeQuery() {
        Assertions.assertEquals("""
                statement.executeQuery()""", generator.executeQuery().toString());
    }

    @Test
    void addBatch() {
        Assertions.assertEquals("""
                statement.addBatch()""", generator.addBatch().toString());
    }

    @Test
    void executeBatch() {
        Assertions.assertEquals("""
                statement.executeBatch()""", generator.executeBatch().toString());
    }

    @Test
    void executeUpdate() {
        Assertions.assertEquals("""
                statement.executeUpdate()""", generator.executeUpdate().toString());
    }

}
