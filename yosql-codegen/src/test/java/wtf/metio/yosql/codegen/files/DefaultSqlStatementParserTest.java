/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.codegen.orchestration.OrchestrationObjectMother;
import wtf.metio.yosql.internals.testing.configs.ConverterConfigurations;
import wtf.metio.yosql.internals.testing.configs.FilesConfigurations;
import wtf.metio.yosql.internals.testing.configs.RepositoriesConfigurations;

import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DefaultSqlStatementParser")
class DefaultSqlStatementParserTest {

    private DefaultSqlStatementParser parser;

    @BeforeEach
    void setUp() {
        parser = new DefaultSqlStatementParser(
                LoggingObjectMother.logger(),
                FilesObjectMother.sqlConfigurationFactory(
                        RepositoriesConfigurations.defaults(),
                        ConverterConfigurations.withConverters()),
                FilesConfigurations.defaults(),
                OrchestrationObjectMother.executionErrors());
    }

    @Test
    void extractParameterIndices() {
        final var indices = SqlStatementParser.extractParameterIndices("""
                INSERT INTO example_table (id, name)
                VALUES (:id, :name);
                """);

        assertAll(
                () -> assertIterableEquals(List.of(1), indices.get("id")),
                () -> assertIterableEquals(List.of(2), indices.get("name")));
    }

    @Test
    void extractMultipleParameterIndices() {
        final var indices = SqlStatementParser.extractParameterIndices("""
                INSERT INTO example_table (id, name, name)
                VALUES (:id, :name, :name);
                """);

        assertAll(
                () -> assertIterableEquals(List.of(1), indices.get("id")),
                () -> assertIterableEquals(List.of(2, 3), indices.get("name")));
    }

    @Test
    void extractUnorderedParameterIndices() {
        final var indices = SqlStatementParser.extractParameterIndices("""
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);
                """);

        assertAll(
                () -> assertIterableEquals(List.of(1, 3), indices.get("id")),
                () -> assertIterableEquals(List.of(2), indices.get("name")));
    }

    @Test
    void extractParameterIndicesInOrder() {
        final var indices = SqlStatementParser.extractParameterIndices("""
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
        final var indices = SqlStatementParser.extractParameterIndices("""
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);
                """);

        final var expectation = new LinkedHashSet<String>();
        expectation.add("id");
        expectation.add("name");
        assertIterableEquals(expectation, indices.keySet());
    }

    @Test
    void parseStatement() {
        final var source = Paths.get("writeData.sql");
        final var statement = """
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);
                """;

        final var sqlStatement = parser.parseStatement(
                source, statement, 1);

        assertAll("statement attributes",
                () -> assertEquals(source, sqlStatement.getSourcePath(), "source"),
                () -> assertEquals(statement, sqlStatement.getRawStatement(), "statement"),
                () -> assertNotNull(sqlStatement.getConfiguration(), "configuration")
        );
    }

    @Test
    void parseCallingStatement() {
        final var statement = """
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);
                """;

        final var sqlStatement = parser.parseStatement(
                Paths.get("callData.sql"), statement, 1);

        assertAll("statement type",
                () -> assertTrue(sqlStatement.isCalling(), "calling"),
                () -> assertFalse(sqlStatement.isReading(), "reading"),
                () -> assertFalse(sqlStatement.isWriting(), "writing")
        );
    }

    @Test
    void parseReadingStatement() {
        final var statement = """
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);
                """;

        final var sqlStatement = parser.parseStatement(
                Paths.get("readData.sql"), statement, 1);

        assertAll("statement type",
                () -> assertFalse(sqlStatement.isCalling(), "calling"),
                () -> assertTrue(sqlStatement.isReading(), "reading"),
                () -> assertFalse(sqlStatement.isWriting(), "writing")
        );
    }

    @Test
    void parseWritingStatement() {
        final var statement = """
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);
                """;

        final var sqlStatement = parser.parseStatement(
                Paths.get("writeData.sql"), statement, 1);

        assertAll("statement type",
                () -> assertFalse(sqlStatement.isCalling(), "calling"),
                () -> assertFalse(sqlStatement.isReading(), "reading"),
                () -> assertTrue(sqlStatement.isWriting(), "writing")
        );
    }

    @Test
    void parseStatementNameFromFile() {
        final var statement = """
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);
                """;

        final var sqlStatement = parser.parseStatement(
                Paths.get("writeData.sql"), statement, 1);

        assertEquals("writeData", sqlStatement.getName());
    }

    @Test
    void parseStatementNameFromFileWithMultipleStatementsInFile() {
        final var statement = """
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);
                """;

        final var sqlStatement = parser.parseStatement(
                Paths.get("writeData.sql"), statement, 3);

        assertEquals("writeData3", sqlStatement.getName());
    }

    @Test
    void parseStatementNameFromFrontMatter() {
        final var statement = """
                -- name: someName
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);
                """;

        final var sqlStatement = parser.parseStatement(
                Paths.get("writeData.sql"), statement, 1);

        assertEquals("someName", sqlStatement.getName());
    }

    @Test
    void getStatementSplitter() {
        final var statement = """
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);;
                INSERT INTO example_table (id, name, id)
                VALUES (:id, :name, :id);;
                """;

        final var splitter = parser.getStatementSplitter("--@yosql sqlStatementSeparator: ;;");
        final var statements = splitter.split(statement);

        assertEquals(3, statements.length);
    }

}
