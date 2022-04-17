/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.testing.configs.Names;

@DisplayName("DefaultJdbcFields")
class DefaultJdbcFieldsTest {

    private DefaultJdbcFields generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcFields(Names.defaults());
    }

    @Test
    void constantSqlStatementFieldName() {
        // given
        final var config = SqlConfiguration.usingDefaults().setName("test").build();

        // when
        final var constant = generator.constantSqlStatementFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST""", constant);
    }

    @Test
    void constantSqlStatementFieldNameWithVendor() {
        // given
        final var config = SqlConfiguration.usingDefaults().setName("test").setVendor("MyDB").build();

        // when
        final var constant = generator.constantSqlStatementFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_MYDB""", constant);
    }

    @Test
    void constantRawSqlStatementFieldName() {
        // given
        final var config = SqlConfiguration.usingDefaults().setName("test").build();

        // when
        final var constant = generator.constantRawSqlStatementFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_RAW""", constant);
    }

    @Test
    void constantRawSqlStatementFieldNameWithVendor() {
        // given
        final var config = SqlConfiguration.usingDefaults().setName("test").setVendor("MyDB").build();

        // when
        final var constant = generator.constantRawSqlStatementFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_MYDB_RAW""", constant);
    }

    @Test
    void constantSqlStatementParameterIndexFieldName() {
        // given
        final var config = SqlConfiguration.usingDefaults().setName("test").build();

        // when
        final var constant = generator.constantSqlStatementParameterIndexFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_INDEX""", constant);
    }

    @Test
    void constantSqlStatementParameterIndexFieldNameWithVendor() {
        // given
        final var config = SqlConfiguration.usingDefaults().setName("test").setVendor("MyDB").build();

        // when
        final var constant = generator.constantSqlStatementParameterIndexFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_MYDB_INDEX""", constant);
    }

}
