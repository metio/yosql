/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DefaultJdbcStatementMethods")
class DefaultJdbcStatementMethodsTest {

    private DefaultJdbcStatementMethods generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcStatementMethods();
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
