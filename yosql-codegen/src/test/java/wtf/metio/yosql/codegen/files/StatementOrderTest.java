/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.codegen.orchestration.OrchestrationObjectMother;
import wtf.metio.yosql.internals.testing.configs.ConverterConfigurations;
import wtf.metio.yosql.internals.testing.configs.FilesConfigurations;
import wtf.metio.yosql.internals.testing.configs.RepositoriesConfigurations;
import wtf.metio.yosql.models.immutables.FilesConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * A statement's position in its file decides what its method is called when several share a name, so
 * the position has to be the one it actually occupies.
 *
 * <p>The file is deliberately large. {@code Pattern.splitAsStream} hands out work in batches of
 * 1024, so a handful of statements never makes the fork-join pool split anything and a race that is
 * always there never shows. Four thousand of them does.</p>
 */
@DisplayName("statement order")
class StatementOrderTest {

    private static final int STATEMENTS = 4_096;

    @TempDir
    Path directory;

    private DefaultSqlStatementParser parser;

    @BeforeEach
    void setUp() {
        parser = new DefaultSqlStatementParser(
                LoggingObjectMother.logger(),
                FilesObjectMother.sqlConfigurationFactory(
                        RepositoriesConfigurations.defaults(),
                        ConverterConfigurations.withConverters()),
                FilesConfiguration.copyOf(FilesConfigurations.defaults()).withInputBaseDirectory(directory),
                OrchestrationObjectMother.executionErrors());
    }

    private Path fileWithManyStatements() throws IOException {
        final var sql = IntStream.range(0, STATEMENTS)
                // An explicit repository keeps the naming configurer from relativising the temp
                // file against the configured input directory; what is under test is the order.
                // No name in the front matter, so each statement is named after the file plus its
                // position — which is exactly the thing that must not depend on the scheduler. An
                // explicit repository keeps the naming configurer from relativising the temp file
                // against the configured input directory.
                .mapToObj(index -> ("-- repository: com.example.persistence.DataRepository\n"
                        + "select col%d from example_table").formatted(index))
                .reduce((left, right) -> left + "\n;\n" + right)
                .orElseThrow();
        final var file = directory.resolve("findData.sql");
        Files.writeString(file, sql + "\n;\n");
        return file;
    }

    @Test
    @DisplayName("the nth statement in a file is numbered n, every run")
    void shouldNumberStatementsInSourceOrder() throws IOException {
        final var file = fileWithManyStatements();

        final var names = parser.parse(file).map(statement -> statement.getName()).toList();

        assertEquals(STATEMENTS, names.size());
        // The first keeps the bare name; the rest carry their position.
        final var expected = IntStream.range(0, STATEMENTS)
                .mapToObj(index -> index == 0 ? "findData" : "findData" + (index + 1))
                .toList();
        assertIterableEquals(expected, names);
    }

    @Test
    @DisplayName("parsing the same file twice produces the same names")
    void shouldBeReproducible() throws IOException {
        final var file = fileWithManyStatements();

        final List<String> first = parser.parse(file).map(statement -> statement.getName()).toList();
        final List<String> second = parser.parse(file).map(statement -> statement.getName()).toList();

        assertIterableEquals(first, second);
    }

}
