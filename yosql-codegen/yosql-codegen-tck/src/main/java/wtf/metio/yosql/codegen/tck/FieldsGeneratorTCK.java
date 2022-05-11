/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.tck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.api.FieldsGenerator;
import wtf.metio.yosql.internals.junit5.TestIterables;
import wtf.metio.yosql.testing.configs.SqlConfigurations;

/**
 * Verifies that {@link FieldsGenerator}s work correctly.
 */
public interface FieldsGeneratorTCK {

    FieldsGenerator generator();

    String staticInitializerExpectation();

    Iterable<String> asFieldsExpectations();

    Iterable<String> asFieldsWithCustomConverterExpectations();

    Iterable<String> asFieldsWithMultipleStatementsExpectations();

    Iterable<String> asFieldsWithMultipleStatementsAndCustomConverterExpectations();

    Iterable<String> asFieldsWithMultipleStatementsAndMixedConverterExpectations();

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
        TestIterables.assertIterables(
                asFieldsExpectations(),
                generator().asFields(SqlConfigurations.sqlStatement()));
    }

    @Test
    default void asFieldsWithCustomConverter() {
        TestIterables.assertIterables(
                asFieldsWithCustomConverterExpectations(),
                generator().asFields(SqlConfigurations.sqlStatementWithCustomConverter()));
    }

    @Test
    default void asFieldsWithMultipleStatements() {
        TestIterables.assertIterables(
                asFieldsWithMultipleStatementsExpectations(),
                generator().asFields(SqlConfigurations.sqlStatements()));
    }

    @Test
    default void asFieldsWithMultipleStatementsAndCustomConverter() {
        TestIterables.assertIterables(
                asFieldsWithMultipleStatementsAndCustomConverterExpectations(),
                generator().asFields(SqlConfigurations.sqlStatementsWithCustomConverter()));
    }

    @Test
    default void asFieldsWithMultipleStatementsAndMixedConverter() {
        TestIterables.assertIterables(
                asFieldsWithMultipleStatementsAndMixedConverterExpectations(),
                generator().asFields(SqlConfigurations.sqlStatementsWithMixedConverter()));
    }

}
