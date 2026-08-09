/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.example.maven.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * What the mojo's own parameter surface actually reaches.
 *
 * <p>Every frontend's surface is generated from the same meta-model and then wired up once per
 * frontend, so a group that arrives through the Maven mojo says nothing about the same group
 * arriving through the task. The build's second execution sets every one of them to something
 * other than its default; these read the generated source back and say so.</p>
 *
 * <p>Reads the source rather than loading the classes, because two of the settings — the annotation
 * and the logging API — are visible in what was written and not in what it compiles to.</p>
 */
@DisplayName("what the mojo parameters reach")
class GeneratedFromTheMojoTest {

    private static final Path GENERATED = Path.of("target", "generated-sources", "config");
    private static final String PERSISTENCE = "wtf/metio/yosql/example/maven/jdbc/config/persistence";

    @Test
    @DisplayName("repositories are written to the package the parameters named")
    void shouldFollowTheBasePackageName() {
        assertTrue(Files.isRegularFile(GENERATED.resolve(PERSISTENCE + "/DocumentRepository.java")),
                "no DocumentRepository under " + PERSISTENCE);
    }

    @Test
    @DisplayName("converters land beside the repositories, named as the parameters asked")
    void shouldFollowTheConverterNaming() {
        // The prefix and suffix are set; the package is not, so this is also the default that puts
        // converters under the repositories' own package rather than an example's.
        final var converter = GENERATED.resolve(PERSISTENCE + "/converter/BuildDocumentMapper.java");

        assertAll(
                () -> assertTrue(Files.isRegularFile(converter), "no BuildDocumentMapper: " + converter),
                () -> assertTrue(read(converter).contains("asRecord("), read(converter)));
    }

    @Test
    @DisplayName("the schema is read from where the parameters pointed, for the vendor it named")
    void shouldReadTheSchemaForTheVendor() {
        // The DDL lives outside the statements directory, so only sqlStatementsDirectory can have
        // found it, and jsonb resolves to a String only for a declared vendor. A record holding one
        // is both settings arriving; either missing fails the build before this runs.
        final var document = read(GENERATED.resolve(
                "wtf/metio/yosql/example/maven/jdbc/config/domain/Document.java"));

        assertAll(
                () -> assertTrue(document.contains("record Document("), document),
                () -> assertTrue(document.contains("String payload"), document),
                () -> assertTrue(document.contains("int revision"), document));
    }

    @Test
    @DisplayName("a parameter takes the type of the column the schema describes")
    void shouldInferParameterTypesFromTheSchema() {
        assertTrue(read(GENERATED.resolve(PERSISTENCE + "/DocumentRepository.java"))
                        .contains("findDocument(final UUID id)"),
                "the id parameter did not come out a UUID");
    }

    @Test
    @DisplayName("the nested annotation parameter ends up on the repository")
    void shouldApplyRepositoryAnnotations() {
        // Elements within elements, which is what the other frontends each spell their own way —
        // a container in Gradle, a nested element in Ant, one packed string on a command line.
        assertTrue(read(GENERATED.resolve(PERSISTENCE + "/DocumentRepository.java"))
                        .contains("maven-config-example"),
                "the repository carries no annotation from the parameters");
    }

    @Test
    @DisplayName("the logging API the parameters chose is the one generated code uses")
    void shouldUseTheChosenLoggingApi() {
        assertTrue(read(GENERATED.resolve(PERSISTENCE + "/DocumentRepository.java"))
                        .contains("java.util.logging"),
                "the repository does not log through java.util.logging");
    }

    private static String read(final Path path) {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new UncheckedIOException("cannot read " + path, exception);
        }
    }

}
