/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.immutables.SqlStatement;
import wtf.metio.yosql.testing.configs.Files;
import wtf.metio.yosql.testing.configs.Sql;

import java.nio.file.Paths;
import java.util.List;

@DisplayName("DefaultJavadoc")
class DefaultJavadocTest {

    private DefaultJavadoc generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJavadoc(Files.defaults());
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
                """, generator.methodJavadoc(List.of()).toString());
    }

    @Test
    @DisplayName("generate class comment with statements")
    void shouldGenerateClassCommentWithStatement() {
        // given
        final List<SqlStatement> statements = List.of(SqlStatement.builder()
                .setConfiguration(Sql.sqlConfiguration())
                .setRawStatement("SELECT 1")
                .setSourcePath(Paths.get("test"))
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
                .setConfiguration(Sql.sqlConfiguration())
                .setRawStatement("SELECT 1")
                .setSourcePath(Paths.get("test"))
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
