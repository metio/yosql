/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.example.gradle.config;

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
 * What the Gradle DSL actually reaches.
 *
 * <p>Every frontend's configuration surface is generated from the same meta-model and then wired up
 * by hand, once per frontend. A setting that works through Maven says nothing about the same setting
 * through Gradle — and the Maven reactor never builds this project, so nothing else here can notice
 * a Gradle initializer that stopped passing a value on. The build script sets every group to
 * something other than its default; these read the generated source back and say so.</p>
 *
 * <p>Reads the source rather than loading the classes, because two of the settings — the annotation
 * and the logging API — are visible in what was written and not in what it compiles to.</p>
 */
@DisplayName("what the Gradle DSL reaches")
class GeneratedFromTheDslTest {

    private static final Path GENERATED = Path.of(System.getProperty("yosql.generated"));
    private static final String PERSISTENCE = "wtf/metio/yosql/example/gradle/config/persistence";

    @Test
    @DisplayName("repositories are written to the package the DSL named")
    void shouldFollowTheBasePackageName() {
        assertTrue(Files.isRegularFile(GENERATED.resolve(PERSISTENCE + "/DocumentRepository.java")),
                "no DocumentRepository under " + PERSISTENCE);
    }

    @Test
    @DisplayName("converters land beside the repositories, named as the DSL asked")
    void shouldFollowTheConverterNaming() {
        // The prefix and suffix are set; the package is not, so this is also the default that puts
        // converters under the repositories' own package rather than an example's.
        final var converter = GENERATED.resolve(PERSISTENCE + "/converter/BuildDocumentMapper.java");

        assertAll(
                () -> assertTrue(Files.isRegularFile(converter), "no BuildDocumentMapper: " + converter),
                () -> assertTrue(read(converter).contains("asRecord("), read(converter)));
    }

    @Test
    @DisplayName("the schema is read from where the DSL pointed, for the vendor it named")
    void shouldReadTheSchemaForTheVendor() {
        // The DDL lives outside src/main/yosql, so only sqlStatementsDirectory can have found it,
        // and jsonb resolves to a String only for a declared vendor. A record holding one is both
        // settings arriving; either missing fails the build before this runs.
        final var document = read(GENERATED.resolve(
                "wtf/metio/yosql/example/gradle/config/domain/Document.java"));

        assertAll(
                () -> assertTrue(document.contains("record Document("), document),
                () -> assertTrue(document.contains("String payload"), document),
                () -> assertTrue(document.contains("int revision"), document));
    }

    @Test
    @DisplayName("a parameter takes the type of the column the schema describes")
    void shouldInferParameterTypesFromTheSchema() {
        // Nothing in the front matter says what :id is, so a UUID here is the catalog answering
        // through Gradle rather than through the Maven mojo that is tested everywhere else.
        assertTrue(read(GENERATED.resolve(PERSISTENCE + "/DocumentRepository.java"))
                        .contains("findDocument(final UUID id)"),
                "the id parameter did not come out a UUID");
    }

    @Test
    @DisplayName("the annotation container puts its annotation on the repository")
    void shouldApplyRepositoryAnnotations() {
        // NamedDomainObjectContainer is a shape only the Gradle DSL has: no other frontend takes an
        // annotation with members this way, so nothing else can be failing in its place.
        assertTrue(read(GENERATED.resolve(PERSISTENCE + "/DocumentRepository.java"))
                        .contains("gradle-config-example"),
                "the repository carries no annotation from the DSL");
    }

    @Test
    @DisplayName("the logging API the DSL chose is the one generated code uses")
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
