/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator.blocks.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.tooling.codegen.test.ObjectMother;

@DisplayName("DefaultJdbcParameters")
class DefaultJdbcParametersTest {

    private DefaultJdbcParameters generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcParameters(
                ObjectMother.parameters(),
                ObjectMother.jdbcNames());
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
                final java.sql.ResultSetMetaData metaData""", generator.metaData().toString());
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