/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.blocks.BlocksObjectMother;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;

@DisplayName("DefaultJdbcParameters")
class DefaultJdbcParametersTest {

    private DefaultJdbcParameters generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcParameters(BlocksObjectMother.parameters(), NamesConfigurations.defaults());
    }

    @Test
    void dataSource() {
        Assertions.assertEquals("""
                final javax.sql.DataSource dataSource""", generator.dataSource().toString());
    }

    @Test
    void connection() {
        Assertions.assertEquals("""
                final java.sql.Connection connection""", generator.connection().toString());
    }

    @Test
    void preparedStatement() {
        Assertions.assertEquals("""
                final java.sql.PreparedStatement statement""", generator.preparedStatement().toString());
    }

    @Test
    void resultSet() {
        Assertions.assertEquals("""
                final java.sql.ResultSet resultSet""", generator.resultSet().toString());
    }

    @Test
    void metaData() {
        Assertions.assertEquals("""
                final java.sql.ResultSetMetaData resultSetMetaData""", generator.resultSetMetaData().toString());
    }

    @Test
    void columnCount() {
        Assertions.assertEquals("""
                final int columnCount""", generator.columnCount().toString());
    }

    @Test
    void index() {
        Assertions.assertEquals("""
                final int index""", generator.index().toString());
    }

    @Test
    void columnLabel() {
        Assertions.assertEquals("""
                final java.lang.String columnLabel""", generator.columnLabel().toString());
    }

}
