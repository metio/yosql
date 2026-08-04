/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import ch.qos.cal10n.MessageConveyor;
import org.junit.jupiter.api.*;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.internals.testing.configs.FilesConfigurations;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.nio.file.Paths;
import java.util.List;

@DisplayName("DefaultJavadoc")
class DefaultJavadocTest {

    @Nested
    @DisplayName("English")
    class English {

        private DefaultJavadoc generator;

        @BeforeEach
        void setUp() {
            generator = new DefaultJavadoc(FilesConfigurations.defaults(), new MessageConveyor(SupportedLocales.ENGLISH));
        }

        @Test
        @DisplayName("generate class comment")
        void shouldGenerateClassComment() {
            Assertions.assertEquals("""
                    Generated based on the following file(s):
                    <ul>
                    </ul>
                    """, generator.repositoryJavadoc(List.of()).toString());
        }

        @Test
        @DisplayName("generate method comment")
        void shouldGenerateMethodComment() {
            Assertions.assertEquals("""
                    <p>Executes the following statement:</p>
                                    
                    <p>Generated based on the following file(s):</p>
                    <ul>
                    </ul>
                    <p>Disable generating this method by setting <strong></strong> to <strong>false</strong></p>
                    """, generator.methodJavadoc(List.of(), "").toString());
        }

        private String statementComment(final String sql) {
            return generator.methodJavadoc(List.of(SqlStatement.builder()
                    .setSourcePath(Paths.get("test"))
                    .setConfiguration(SqlConfigurations.sqlConfiguration())
                    .setRawStatement(sql)
                    .build()), "executeOnce").toString();
        }

        @Test
        @DisplayName("shows a statement the way javadoc shows code, so markup in it stays text")
        void showsStatementsAsCode() {
            final var comment = statementComment("select * from t where a < 1 and b > 2 and c & 3 = 0");

            Assertions.assertAll(
                    () -> Assertions.assertTrue(comment.contains("<pre>{@code"), comment),
                    () -> Assertions.assertTrue(comment.contains("a < 1 and b > 2 and c & 3 = 0"), comment),
                    () -> Assertions.assertFalse(comment.contains("&lt;"), comment));
        }

        @Test
        @DisplayName("a JDBC escape keeps its braces, because they balance")
        void keepsBalancedBraces() {
            final var comment = statementComment("{call sp_do_something(?)}");

            Assertions.assertAll(
                    () -> Assertions.assertTrue(comment.contains("<pre>{@code"), comment),
                    () -> Assertions.assertTrue(comment.contains("{call sp_do_something(?)}"), comment));
        }

        @Test
        @DisplayName("an optimizer hint ends the comment, so it is escaped instead")
        void escapesCommentTerminator() {
            final var comment = statementComment("select /*+ INDEX(t idx) */ * from t");

            Assertions.assertAll(
                    () -> Assertions.assertFalse(comment.contains("{@code"), comment),
                    () -> Assertions.assertFalse(comment.contains("*/"), comment),
                    () -> Assertions.assertTrue(comment.contains("*&#47;"), comment));
        }

        @Test
        @DisplayName("an unbalanced brace would close the tag early, so it is escaped instead")
        void escapesUnbalancedBraces() {
            final var comment = statementComment("select '}' from t where a < 1");

            Assertions.assertAll(
                    () -> Assertions.assertFalse(comment.contains("{@code"), comment),
                    () -> Assertions.assertTrue(comment.contains("&lt;"), comment));
        }

        @Test
        @DisplayName("a variable starting a line reads as a block tag, so it is escaped instead")
        void escapesLeadingAtSign() {
            final var comment = statementComment("select\n@rownum := @rownum + 1 as position\nfrom players");

            Assertions.assertAll(
                    () -> Assertions.assertFalse(comment.contains("{@code"), comment),
                    () -> Assertions.assertFalse(comment.contains("\n@rownum"), comment),
                    () -> Assertions.assertTrue(comment.contains("&#64;rownum"), comment));
        }

        @Test
        @DisplayName("a unicode escape is resolved before javac even lexes, so it is escaped instead")
        void escapesUnicodeEscapes() {
            final var comment = statementComment("select * from t where path = 'C:\\usr\\bin'");

            Assertions.assertAll(
                    () -> Assertions.assertFalse(comment.contains("{@code"), comment),
                    () -> Assertions.assertFalse(comment.contains("\\u"),
                            () -> "javac resolves this inside a comment too, and reports "
                                    + "'illegal unicode escape' in a file the author never wrote:\n" + comment),
                    () -> Assertions.assertTrue(comment.contains("&#92;u"), comment));
        }

        @Test
        @DisplayName("the file's own path is escaped like anything else taken from the user")
        void escapesTheSourcePath() {
            final var comment = generator.repositoryJavadoc(List.of(SqlStatement.builder()
                    .setSourcePath(Paths.get("reports*", "find-a&b.sql"))
                    .setConfiguration(SqlConfigurations.sqlConfiguration())
                    .setRawStatement("select 1")
                    .build())).toString();

            Assertions.assertAll(
                    () -> Assertions.assertFalse(comment.contains("&b.sql"),
                            () -> "an unescaped ampersand opens an entity doclint then rejects:\n" + comment),
                    () -> Assertions.assertTrue(comment.contains("&amp;"), comment));
        }

        @Test
        @DisplayName("a backslash that starts nothing is left alone")
        void keepsOrdinaryBackslashes() {
            final var comment = statementComment("select * from t where digits ~ '\\d+'");

            Assertions.assertTrue(comment.contains("\\d"), comment);
        }

        @Test
        @DisplayName("generate class comment with statements")
        void shouldGenerateClassCommentWithStatement() {
            // given
            final List<SqlStatement> statements = List.of(SqlStatement.builder()
                    .setSourcePath(Paths.get("test"))
                    .setConfiguration(SqlConfigurations.sqlConfiguration())
                    .setRawStatement("SELECT 1")
                    .build());

            // when
            final var comment = generator.repositoryJavadoc(statements);

            // then
            Assertions.assertEquals("""
                    Generated based on the following file(s):
                    <ul>
                    <li>test</li>
                    </ul>
                    """, comment.toString());
        }

        @Test
        @DisplayName("generate method comment with statements")
        void shouldGenerateMethodCommentWithStatement() {
            // given
            final List<SqlStatement> statements = List.of(SqlStatement.builder()
                    .setSourcePath(Paths.get("test"))
                    .setConfiguration(SqlConfigurations.sqlConfiguration())
                    .setRawStatement("SELECT 1")
                    .build());

            // when
            final var comment = generator.repositoryJavadoc(statements);

            // then
            Assertions.assertEquals("""
                    Generated based on the following file(s):
                    <ul>
                    <li>test</li>
                    </ul>
                    """, comment.toString());
        }

    }

    @Nested
    @DisplayName("German")
    class German {

        private DefaultJavadoc generator;

        @BeforeEach
        void setUp() {
            generator = new DefaultJavadoc(FilesConfigurations.defaults(), new MessageConveyor(SupportedLocales.GERMAN));
        }

        @Test
        @DisplayName("generate class comment")
        void shouldGenerateClassComment() {
            Assertions.assertEquals("""
                    Die folgende(n) Datei(en) wurden zur Generierung verwendet:
                    <ul>
                    </ul>
                    """, generator.repositoryJavadoc(List.of()).toString());
        }

        @Test
        @DisplayName("generate method comment")
        void shouldGenerateMethodComment() {
            Assertions.assertEquals("""
                    <p>Führt das folgende Statement aus:</p>
                                    
                    Die folgende(n) Datei(en) wurden zur Generierung verwendet:
                    <ul>
                    </ul>
                    <p>Um diese Methode nicht mehr zu generieren, setzen Sie<strong></strong> auf den Wert <strong>false</strong></p>
                    """, generator.methodJavadoc(List.of(), "").toString());
        }

        @Test
        @DisplayName("generate class comment with statements")
        void shouldGenerateClassCommentWithStatement() {
            // given
            final List<SqlStatement> statements = List.of(SqlStatement.builder()
                    .setSourcePath(Paths.get("test"))
                    .setConfiguration(SqlConfigurations.sqlConfiguration())
                    .setRawStatement("SELECT 1")
                    .build());

            // when
            final var comment = generator.repositoryJavadoc(statements);

            // then
            Assertions.assertEquals("""
                    Die folgende(n) Datei(en) wurden zur Generierung verwendet:
                    <ul>
                    <li>test</li>
                    </ul>
                    """, comment.toString());
        }

        @Test
        @DisplayName("generate method comment with statements")
        void shouldGenerateMethodCommentWithStatement() {
            // given
            final List<SqlStatement> statements = List.of(SqlStatement.builder()
                    .setSourcePath(Paths.get("test"))
                    .setConfiguration(SqlConfigurations.sqlConfiguration())
                    .setRawStatement("SELECT 1")
                    .build());

            // when
            final var comment = generator.repositoryJavadoc(statements);

            // then
            Assertions.assertEquals("""
                    Die folgende(n) Datei(en) wurden zur Generierung verwendet:
                    <ul>
                    <li>test</li>
                    </ul>
                    """, comment.toString());
        }

    }

}
