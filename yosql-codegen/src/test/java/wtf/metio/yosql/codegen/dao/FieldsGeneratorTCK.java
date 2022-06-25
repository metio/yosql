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
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Verifies that {@link FieldsGenerator}s work correctly.
 */
public abstract class FieldsGeneratorTCK {

    abstract FieldsGenerator generator();

    abstract String staticInitializerExpectation();

    abstract String asFieldsExpectations();

    abstract String asFieldsWithCustomConverterExpectations();

    abstract String asFieldsWithMultipleStatementsExpectations();

    abstract String asFieldsWithMultipleStatementsAndCustomConverterExpectations();

    abstract String asFieldsWithMultipleStatementsAndMixedConverterExpectations();

    @Test
    final void staticInitializer() {
        final var staticInitializer = generator().staticInitializer(SqlConfigurations.sqlStatement());
        Assumptions.assumeTrue(staticInitializer.isPresent());
        Assertions.assertEquals(
                staticInitializerExpectation(),
                staticInitializer.get().toString(),
                "The generated static initializer does not match expectation");
    }

    @Test
    final void asFields() {
        assertFields(
                asFieldsExpectations(),
                generator().asFields(SqlConfigurations.sqlStatement()));
    }

    @Test
    final void asFieldsWithCustomConverter() {
        assertFields(
                asFieldsWithCustomConverterExpectations(),
                generator().asFields(SqlConfigurations.sqlStatementWithCustomConverter()));
    }

    @Test
    final void asFieldsWithMultipleStatements() {
        assertFields(
                asFieldsWithMultipleStatementsExpectations(),
                generator().asFields(SqlConfigurations.sqlStatements()));
    }

    @Test
    final void asFieldsWithMultipleStatementsAndCustomConverter() {
        assertFields(
                asFieldsWithMultipleStatementsAndCustomConverterExpectations(),
                generator().asFields(SqlConfigurations.sqlStatementsWithCustomConverter()));
    }

    @Test
    final void asFieldsWithMultipleStatementsAndMixedConverter() {
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
    final void constantSqlStatementFieldName() {
        // given
        final var config = SqlConfiguration.builder().setName("test").build();

        // when
        final var constant = generator().constantSqlStatementFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST""", constant);
    }

    @Test
    final void constantSqlStatementFieldNameWithVendor() {
        // given
        final var config = SqlConfiguration.builder().setName("test").setVendor("MyDB").build();

        // when
        final var constant = generator().constantSqlStatementFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_MYDB""", constant);
    }

    @Test
    final void constantRawSqlStatementFieldName() {
        // given
        final var config = SqlConfiguration.builder().setName("test").build();

        // when
        final var constant = generator().constantRawSqlStatementFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_RAW""", constant);
    }

    @Test
    final void constantRawSqlStatementFieldNameWithVendor() {
        // given
        final var config = SqlConfiguration.builder().setName("test").setVendor("MyDB").build();

        // when
        final var constant = generator().constantRawSqlStatementFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_MYDB_RAW""", constant);
    }

    @Test
    final void constantSqlStatementParameterIndexFieldName() {
        // given
        final var config = SqlConfiguration.builder().setName("test").build();

        // when
        final var constant = generator().constantSqlStatementParameterIndexFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_INDEX""", constant);
    }

    @Test
    final void constantSqlStatementParameterIndexFieldNameWithVendor() {
        // given
        final var config = SqlConfiguration.builder().setName("test").setVendor("MyDB").build();

        // when
        final var constant = generator().constantSqlStatementParameterIndexFieldName(config);

        // then
        Assertions.assertEquals("""
                TEST_MYDB_INDEX""", constant);
    }

}
