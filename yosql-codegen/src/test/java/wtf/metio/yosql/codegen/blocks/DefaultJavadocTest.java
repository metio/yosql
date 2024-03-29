/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
