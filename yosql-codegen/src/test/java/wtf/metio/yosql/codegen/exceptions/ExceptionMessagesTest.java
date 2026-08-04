/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Every exception in this package says what went wrong.
 *
 * <p>These are how a build failure reaches the user: {@code ExecutionErrors} logs
 * {@code getMessage()} and nothing else at the default level, so an exception that never calls
 * {@code super(message)} prints a bare {@code null} and the build fails having said nothing. A
 * constructor is easy to leave off and impossible to notice from the call site, which is always
 * {@code orElseThrow(SomeException::new)}.</p>
 *
 * <p>Read from the sources rather than by loading the classes: what is being checked is that
 * somebody wrote the constructor, and a source file is where that is visible without having to
 * guess at arguments for the ones that take them.</p>
 */
@DisplayName("every exception")
class ExceptionMessagesTest {

    private static final Path SOURCES = Path.of("src/main/java/wtf/metio/yosql/codegen/exceptions");

    static Stream<Path> exceptions() {
        try (final var files = Files.list(SOURCES)) {
            return files.filter(file -> file.getFileName().toString().endsWith("Exception.java"))
                    .sorted()
                    .toList()
                    .stream();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("exceptions")
    @DisplayName("passes a message to its superclass")
    void carriesAMessage(final Path source) throws IOException {
        final var code = Files.readString(source, StandardCharsets.UTF_8);
        Assertions.assertTrue(code.contains("super("),
                () -> source.getFileName() + " declares no constructor passing a message, so a build "
                        + "that throws it prints 'null' and says nothing about what is wrong");
    }

}
