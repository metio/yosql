package wtf.metio.yosql.generator.blocks.javadoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.model.sql.SqlStatement;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import java.nio.file.Paths;
import java.util.List;

@DisplayName("DefaultJavadoc")
class DefaultJavadocTest extends ValidationFileTest {
    
    private DefaultJavadoc generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJavadoc();
    }

    @Test
    @DisplayName("generate class comment")
    void shouldGenerateClassComment(final ValidationFile validationFile) {
        validate(generator.repositoryJavadoc(List.of()), validationFile);
    }

    @Test
    @DisplayName("generate method comment")
    void shouldGenerateMethodComment(final ValidationFile validationFile) {
        validate(generator.methodJavadoc(List.of()), validationFile);
    }

    @Test
    @DisplayName("generate class comment with statements")
    void shouldGenerateClassCommentWithStatement(final ValidationFile validationFile) {
        // given
        final List<SqlStatement> statements = List.of(
                new SqlStatement(Paths.get("test"), new SqlConfiguration(), "SELECT 1"));

        // when
        final var comment = generator.repositoryJavadoc(statements);

        // then
        validate(comment, validationFile);
    }

    @Test
    @DisplayName("generate method comment with statements")
    void shouldGenerateMethodCommentWithStatement(final ValidationFile validationFile) {
        // given
        final List<SqlStatement> statements = List.of(
                new SqlStatement(Paths.get("test"), new SqlConfiguration(), "SELECT 1"));

        // when
        final var comment = generator.repositoryJavadoc(statements);

        // then
        validate(comment, validationFile);
    }

}