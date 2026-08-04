/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.palantir.javapoet.FieldSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Vendor variants of one statement share a single generated method. Its signature is the union of every
 * variant's parameters, while the index map it reads is picked per vendor, so each variant's map has to
 * answer for every parameter of that union - otherwise the binding loop iterates null on whichever
 * database picked the variant that does not bind it, and the failure is database-dependent.
 */
@DisplayName("parameter indices across vendor variants")
final class VendorParameterIndexTest {

    @Test
    @DisplayName("a vendor that does not bind a parameter still holds an entry for it")
    void everyVendorAnswersForEveryParameter() {
        final var initializer = DaoObjectMother.fieldsGenerator()
                .staticInitializer(SqlConfigurations.sqlStatementsWithDifferingVendorParameters())
                .orElseThrow()
                .toString();
        assertAll("static initializer",
                () -> assertTrue(initializer.contains("QUERY_DATA_H2_INDEX.put(\"test\", new int[] { })"),
                        () -> "H2 binds no 'test', so its map needs an empty entry:\n" + initializer),
                () -> assertTrue(initializer.contains("QUERY_DATA_H2_INDEX.put(\"id\", new int[] { 1 })"),
                        () -> "H2 binds 'id':\n" + initializer),
                () -> assertTrue(initializer.contains("QUERY_DATA_POSTGRESQL_INDEX.put(\"test\", new int[] { 0 })"),
                        () -> "PostgreSQL binds 'test':\n" + initializer));
    }

    @Test
    @DisplayName("a vendor that binds nothing at all still gets an index map")
    void aVendorWithoutParametersStillGetsAMap() {
        final var statements = SqlConfigurations.sqlStatementsWithVendorWithoutParameters();
        final var fields = fieldNames();
        assertAll("fields",
                () -> assertTrue(fields.contains("QUERY_DATA_H2_INDEX"),
                        () -> "without a map of its own the switch leaves index null: " + fields),
                () -> assertTrue(fields.contains("QUERY_DATA_POSTGRESQL_INDEX"),
                        () -> "the binding vendor needs its map: " + fields));
        final var initializer = DaoObjectMother.fieldsGenerator()
                .staticInitializer(statements)
                .orElseThrow()
                .toString();
        assertTrue(initializer.contains("QUERY_DATA_H2_INDEX.put(\"id\", new int[] { })"),
                () -> "the non-binding vendor answers 'id' with nothing to bind:\n" + initializer);
    }

    private static String fieldNames() {
        return StreamSupport
                .stream(DaoObjectMother.fieldsGenerator()
                        .asFields(SqlConfigurations.sqlStatementsWithVendorWithoutParameters())
                        .spliterator(), false)
                .map(FieldSpec::name)
                .collect(Collectors.joining(", "));
    }

}
