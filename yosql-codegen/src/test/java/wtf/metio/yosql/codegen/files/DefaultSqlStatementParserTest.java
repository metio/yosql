/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.codegen.orchestration.OrchestrationObjectMother;
import wtf.metio.yosql.internals.testing.configs.ConverterConfigurations;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import wtf.metio.yosql.models.immutables.SqlStatement;
import wtf.metio.yosql.internals.testing.configs.FilesConfigurations;
import wtf.metio.yosql.internals.testing.configs.RepositoriesConfigurations;

import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DefaultSqlStatementParser")
class DefaultSqlStatementParserTest {

    /**
     * The front matter every statement below needs: a parameter the generator cannot type fails the
     * build, and nothing here names a record to take a type from.
     */
    private static final String TYPED_PARAMETERS = """
            -- parameters:
            --   id: int
            --   name: string
            """;

    private static final String SQL = """
            INSERT INTO example_table (id, name, id)
            VALUES (:id, :name, :id);
            """;

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

    @Nested
    @DisplayName("the separator between statements")
    class Separator {

        @TempDir
        Path directory;

        private List<SqlStatement> parse(final String content) throws IOException {
            final var file = directory.resolve("findTenant.sql");
            Files.writeString(file, content);
            return parser.parse(file).toList();
        }

        @Test
        @DisplayName("is not a separator inside a string literal")
        void shouldNotSplitInsideAStringLiteral() throws IOException {
            final var statements = parse("""
                    -- name: findTenant
                    -- returning: multiple
                    -- repository: com.example.persistence.TenantRepository
                    select split_part(name, ';', 1) from tenant
                    """);

            assertAll(
                    () -> assertEquals(1, statements.size(), () -> "cut into " + statements.size() + " statements"),
                    () -> assertEquals("select split_part(name, ';', 1) from tenant",
                            statements.getFirst().getRawStatement().strip()));
        }

        @Test
        @DisplayName("is not a separator inside a block comment")
        void shouldNotSplitInsideABlockComment() throws IOException {
            final var statements = parse("""
                    -- name: findTenant
                    -- returning: multiple
                    -- repository: com.example.persistence.TenantRepository
                    select id /* one; two */ from tenant
                    """);

            assertAll(
                    () -> assertEquals(1, statements.size(), () -> "cut into " + statements.size() + " statements"),
                    () -> assertEquals("select id /* one; two */ from tenant",
                            statements.getFirst().getRawStatement().strip()));
        }

        @Test
        @DisplayName("still separates the statements it stands between")
        void shouldStillSplitOrdinaryStatements() throws IOException {
            final var statements = parse("""
                    -- name: findTenant
                    -- returning: multiple
                    -- repository: com.example.persistence.TenantRepository
                    select id from tenant;

                    -- name: findAccount
                    -- returning: multiple
                    -- repository: com.example.persistence.TenantRepository
                    select id from account where label = 'a;b'
                    """);

            assertAll(
                    () -> assertEquals(2, statements.size()),
                    () -> assertEquals("findTenant", statements.getFirst().getName()),
                    () -> assertEquals("findAccount", statements.getLast().getName()),
                    () -> assertEquals("select id from account where label = 'a;b'",
                            statements.getLast().getRawStatement().strip()));
        }

    }

    @Nested
    @DisplayName("the licence header a file opens with")
    class Header {

        @TempDir
        Path directory;

        private List<SqlStatement> parse(final String content) throws IOException {
            final var file = directory.resolve("findTenant.sql");
            Files.writeString(file, content);
            return parser.parse(file).toList();
        }

        @Test
        @DisplayName("is dropped however many lines it runs to")
        void shouldDropABlockComment() throws IOException {
            final var statements = parse("""
                    /*
                     * SPDX-FileCopyrightText: The yosql Authors
                     * SPDX-License-Identifier: 0BSD
                     *
                     * A header nobody has to count the lines of.
                     */

                    -- name: findTenant
                    -- returning: multiple
                    -- repository: com.example.persistence.TenantRepository
                    select id from tenant
                    """);

            assertAll(
                    () -> assertEquals(1, statements.size()),
                    () -> assertEquals("findTenant", statements.getFirst().getName()),
                    () -> assertEquals("select id from tenant", statements.getFirst().getRawStatement().strip()));
        }

        @Test
        @DisplayName("is left alone where it is a vendor's hint rather than a header")
        void shouldKeepACommentInsideTheStatement() throws IOException {
            final var statements = parse("""
                    -- name: findTenant
                    -- returning: multiple
                    -- repository: com.example.persistence.TenantRepository
                    select /*+ INDEX(tenant tenant_pkey) */ id from tenant
                    """);

            assertTrue(statements.getFirst().getRawStatement().contains("/*+ INDEX(tenant tenant_pkey) */"),
                    statements.getFirst().getRawStatement());
        }

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
    @DisplayName("a colon with no name after it is punctuation, not a parameter")
    void ignoreColonsThatNameNothing() {
        final var indices = SqlStatementParser.extractParameterIndices("""
                /*
                 * SPDX-FileCopyrightText: The yosql Authors
                 * SPDX-License-Identifier: 0BSD
                 */
                SELECT * FROM example_table WHERE id = :id;
                """);

        assertAll(
                () -> assertIterableEquals(List.of("id"), indices.keySet()),
                () -> assertIterableEquals(List.of(1), indices.get("id")));
    }

    @Test
    @DisplayName("a cast is not a parameter, and does not take an index from the ones that are")
    void ignorePostgresCasts() {
        final var indices = SqlStatementParser.extractParameterIndices("""
                SELECT payload::text
                FROM document
                WHERE tenant_id = :tenantId
                  AND id = :id;
                """);

        assertAll(
                () -> assertIterableEquals(List.of("tenantId", "id"), indices.keySet()),
                () -> assertIterableEquals(List.of(1), indices.get("tenantId")),
                () -> assertIterableEquals(List.of(2), indices.get("id")));
    }

    @Test
    @DisplayName("a colon inside a string literal is part of the string")
    void ignoreColonsInsideStringLiterals() {
        final var indices = SqlStatementParser.extractParameterIndices("""
                SELECT to_char(created_at, 'YYYY-MM-DD HH24:MI:SS')
                FROM document
                WHERE id = :id;
                """);

        assertAll(
                () -> assertIterableEquals(List.of("id"), indices.keySet()),
                () -> assertIterableEquals(List.of(1), indices.get("id")));
    }

    @Test
    @DisplayName("a question mark inside a string literal is part of the string")
    void ignoreQuestionMarksInsideStringLiterals() {
        final var indices = SqlStatementParser.extractParameterIndices("""
                SELECT * FROM document WHERE title = 'why?' AND id = :id;
                """);

        assertAll(
                () -> assertIterableEquals(List.of("id"), indices.keySet()),
                () -> assertIterableEquals(List.of(1), indices.get("id")));
    }

    @Test
    @DisplayName("both spellings of a placeholder share one sequence of indices")
    void numberEverySpellingTogether() {
        final var indices = SqlStatementParser.extractParameterIndices("""
                SELECT * FROM example_table WHERE a = ? AND b = :b AND c = ?;
                """);

        assertAll(
                () -> assertIterableEquals(List.of(1, 3), indices.get(SqlStatementParser.UNNAMED_PARAMETERS)),
                () -> assertIterableEquals(List.of(2), indices.get("b")));
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

        final var sqlStatement = parser.parseStatement(
                source, TYPED_PARAMETERS + SQL, 1);

        assertAll("statement attributes",
                () -> assertEquals(source, sqlStatement.getSourcePath(), "source"),
                () -> assertEquals(SQL, sqlStatement.getRawStatement(), "statement"),
                () -> assertNotNull(sqlStatement.getConfiguration(), "configuration")
        );
    }

    @Test
    void parseCallingStatement() {
        final var sqlStatement = parser.parseStatement(
                Paths.get("callData.sql"), TYPED_PARAMETERS + SQL, 1);

        assertAll("statement type",
                () -> assertTrue(sqlStatement.isCalling(), "calling"),
                () -> assertFalse(sqlStatement.isReading(), "reading"),
                () -> assertFalse(sqlStatement.isWriting(), "writing")
        );
    }

    @Test
    void parseReadingStatement() {
        final var sqlStatement = parser.parseStatement(
                Paths.get("readData.sql"), TYPED_PARAMETERS + SQL, 1);

        assertAll("statement type",
                () -> assertFalse(sqlStatement.isCalling(), "calling"),
                () -> assertTrue(sqlStatement.isReading(), "reading"),
                () -> assertFalse(sqlStatement.isWriting(), "writing")
        );
    }

    @Test
    void parseWritingStatement() {
        final var sqlStatement = parser.parseStatement(
                Paths.get("writeData.sql"), TYPED_PARAMETERS + SQL, 1);

        assertAll("statement type",
                () -> assertFalse(sqlStatement.isCalling(), "calling"),
                () -> assertFalse(sqlStatement.isReading(), "reading"),
                () -> assertTrue(sqlStatement.isWriting(), "writing")
        );
    }

    @Test
    void parseStatementNameFromFile() {
        final var sqlStatement = parser.parseStatement(
                Paths.get("writeData.sql"), TYPED_PARAMETERS + SQL, 1);

        assertEquals("writeData", sqlStatement.getName());
    }

    @Test
    void parseStatementNameFromFileWithMultipleStatementsInFile() {
        final var sqlStatement = parser.parseStatement(
                Paths.get("writeData.sql"), TYPED_PARAMETERS + SQL, 3);

        assertEquals("writeData3", sqlStatement.getName());
    }

    @Test
    void parseStatementNameFromFrontMatter() {
        final var statement = "-- name: someName\n" + TYPED_PARAMETERS + SQL;

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
