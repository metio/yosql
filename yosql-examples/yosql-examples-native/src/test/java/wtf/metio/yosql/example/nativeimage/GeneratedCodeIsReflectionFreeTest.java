/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.nativeimage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Reads the generated sources and fails if anything in them would have to be resolved at runtime.
 *
 * <p>The native run in CI catches a missing registration the expensive way, by failing on the code
 * path that needed it. This catches the same regression in a second, without a GraalVM toolchain,
 * so a change that reintroduces reflection is caught by the ordinary gate rather than by the one
 * job that downloads a compiler.</p>
 */
@DisplayName("generated code")
class GeneratedCodeIsReflectionFreeTest {

    private static final Path GENERATED = Path.of("target", "generated-sources", "yosql");

    private static List<Path> generatedSources() {
        try (final var files = Files.walk(GENERATED)) {
            return files.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .toList();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private static String contentOf(final Path path) {
        try {
            return Files.readString(path);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    @Test
    @DisplayName("exists at all, so the checks below are checking something")
    void generatedSourcesExist() {
        final var sources = generatedSources();
        assertFalse(sources.isEmpty(), "no generated sources under " + GENERATED.toAbsolutePath());
        assertTrue(sources.stream().anyMatch(path -> path.getFileName().toString().equals("ToReadingConverter.java")),
                () -> "no converter was generated: " + sources);
    }

    @ParameterizedTest
    @DisplayName("names nothing that resolves a type, member or constructor at runtime")
    @ValueSource(strings = {
            "java.lang.reflect",
            "Class.forName",
            "getDeclaredConstructor",
            "getDeclaredMethod",
            "getDeclaredField",
            "MethodHandles",
            "java.lang.invoke",
            "ServiceLoader",
            "Proxy.newProxyInstance",
            ".newInstance(",
    })
    void carriesNoReflection(final String marker) {
        final var offenders = generatedSources().stream()
                .filter(path -> contentOf(path).contains(marker))
                .toList();
        assertTrue(offenders.isEmpty(), () -> "'" + marker + "' appears in " + offenders);
    }

    @Test
    @DisplayName("reads every column through a typed getter, never through a bare getObject")
    void readsThroughTypedGetters() {
        // `getObject(column)` hands back whatever the driver decided, and the caller then has to
        // work out what it got. Every read is either a typed getter or getObject with a class
        // literal, both of which the compiler has already resolved.
        final var offenders = converters()
                .filter(source -> source.matches("(?s).*getObject\\(\"[^\"]+\"\\)\\s*[;)].*"))
                .toList();
        assertTrue(offenders.isEmpty(), () -> "untyped getObject in " + offenders);
    }

    private static Stream<String> converters() {
        return generatedSources().stream()
                .filter(path -> path.toString().contains("converter"))
                .filter(path -> !path.getFileName().toString().equals("ToMapConverter.java"))
                .map(GeneratedCodeIsReflectionFreeTest::contentOf);
    }

}
