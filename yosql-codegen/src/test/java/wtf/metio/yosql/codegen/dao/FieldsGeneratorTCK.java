/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.FieldSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.testing.configs.SqlConfigurations;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Verifies that {@link FieldsGenerator}s work correctly.
 */
public interface FieldsGeneratorTCK {

    FieldsGenerator generator();

    String staticInitializerExpectation();

    String asFieldsExpectations();

    String asFieldsWithCustomConverterExpectations();

    String asFieldsWithMultipleStatementsExpectations();

    String asFieldsWithMultipleStatementsAndCustomConverterExpectations();

    String asFieldsWithMultipleStatementsAndMixedConverterExpectations();

    @Test
    default void staticInitializer() {
        final var staticInitializer = generator().staticInitializer(SqlConfigurations.sqlStatement());
        Assumptions.assumeTrue(staticInitializer.isPresent());
        Assertions.assertEquals(
                staticInitializerExpectation(),
                staticInitializer.get().toString(),
                "The generated static initializer does not match expectation");
    }

    @Test
    default void asFields() {
        assertFields(
                asFieldsExpectations(),
                generator().asFields(SqlConfigurations.sqlStatement()));
    }

    @Test
    default void asFieldsWithCustomConverter() {
        assertFields(
                asFieldsWithCustomConverterExpectations(),
                generator().asFields(SqlConfigurations.sqlStatementWithCustomConverter()));
    }

    @Test
    default void asFieldsWithMultipleStatements() {
        assertFields(
                asFieldsWithMultipleStatementsExpectations(),
                generator().asFields(SqlConfigurations.sqlStatements()));
    }

    @Test
    default void asFieldsWithMultipleStatementsAndCustomConverter() {
        assertFields(
                asFieldsWithMultipleStatementsAndCustomConverterExpectations(),
                generator().asFields(SqlConfigurations.sqlStatementsWithCustomConverter()));
    }

    @Test
    default void asFieldsWithMultipleStatementsAndMixedConverter() {
        assertFields(
                asFieldsWithMultipleStatementsAndMixedConverterExpectations(),
                generator().asFields(SqlConfigurations.sqlStatementsWithMixedConverter()));
    }

    private static void assertFields(final String expectedValue, final Iterable<FieldSpec> fields) {
        Assertions.assertEquals(expectedValue, StreamSupport.stream(fields.spliterator(), false)
                .map(Object::toString)
                .collect(Collectors.joining("\n")));
    }

    @Test
    default void constantSqlStatementFieldName() {
        // given
        final var config = SqlConfiguration.usingDefaults().setName("test").build();

        // when
        final var constant = generator().constantSqlStatementFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST""", constant);
    }

    @Test
    default void constantSqlStatementFieldNameWithVendor() {
        // given
        final var config = SqlConfiguration.usingDefaults().setName("test").setVendor("MyDB").build();

        // when
        final var constant = generator().constantSqlStatementFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_MYDB""", constant);
    }

    @Test
    default void constantRawSqlStatementFieldName() {
        // given
        final var config = SqlConfiguration.usingDefaults().setName("test").build();

        // when
        final var constant = generator().constantRawSqlStatementFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_RAW""", constant);
    }

    @Test
    default void constantRawSqlStatementFieldNameWithVendor() {
        // given
        final var config = SqlConfiguration.usingDefaults().setName("test").setVendor("MyDB").build();

        // when
        final var constant = generator().constantRawSqlStatementFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_MYDB_RAW""", constant);
    }

    @Test
    default void constantSqlStatementParameterIndexFieldName() {
        // given
        final var config = SqlConfiguration.usingDefaults().setName("test").build();

        // when
        final var constant = generator().constantSqlStatementParameterIndexFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_INDEX""", constant);
    }

    @Test
    default void constantSqlStatementParameterIndexFieldNameWithVendor() {
        // given
        final var config = SqlConfiguration.usingDefaults().setName("test").setVendor("MyDB").build();

        // when
        final var constant = generator().constantSqlStatementParameterIndexFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_MYDB_INDEX""", constant);
    }

}
