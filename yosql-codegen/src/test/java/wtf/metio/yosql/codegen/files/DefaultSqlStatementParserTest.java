/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testing.configs.ConverterConfigurations;
import wtf.metio.yosql.testing.configs.RepositoriesConfigurations;

import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class DefaultSqlStatementParserTest {

    private SqlStatementParser parser;

    @BeforeEach
    void setUp() {
        parser = FilesObjectMother.sqlStatementParser(
                RepositoriesConfigurations.defaults(),
                ConverterConfigurations.withConverters());
    }

    @Test
    void extractParameterIndices() {
        final var indices = parser.extractParameterIndices("""
                INSERT INTO example_table (id, name)
                VALUES (:id, :name);
                """);

        assertAll(
                () -> assertIterableEquals(List.of(1), indices.get("id")),
                () -> assertIterableEquals(List.of(2), indices.get("name")));
    }

    @Test
    void extractMultipleParameterIndices() {
        final var indices = parser.extractParameterIndices("""
                INSERT INTO example_table (id, name, name)
                VALUES (:id, :name, :name);
                """);

        assertAll(
                () -> assertIterableEquals(List.of(1), indices.get("id")),
                () -> assertIterableEquals(List.of(2, 3), indices.get("name")));
    }

    @Test
    void extractUnorderedParameterIndices() {
        final var indices = parser.extractParameterIndices("""
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);
                """);

        assertAll(
                () -> assertIterableEquals(List.of(1, 3), indices.get("id")),
                () -> assertIterableEquals(List.of(2), indices.get("name")));
    }

    @Test
    void extractParameterIndicesInOrder() {
        final var indices = parser.extractParameterIndices("""
                INSERT INTO example_table (id, name)
                VALUES (:id, :name);
                """);

        final var expectation = new LinkedHashSet<String>();
        expectation.add("id");
        expectation.add("name");
        assertIterableEquals(expectation, indices.keySet());
    }

    @Test
    void extractMultipleParameterIndicesInOrder() {
        final var indices = parser.extractParameterIndices("""
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);
                """);

        final var expectation = new LinkedHashSet<String>();
        expectation.add("id");
        expectation.add("name");
        assertIterableEquals(expectation, indices.keySet());
    }

}
