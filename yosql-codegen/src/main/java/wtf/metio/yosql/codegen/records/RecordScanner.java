/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.palantir.javapoet.ClassName;
import wtf.metio.yosql.models.immutables.FilesConfiguration;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Finds the source of a type under the configured source directory and reads its shape.
 *
 * <p>Results are remembered for the run: a value object shared by several records is read once,
 * however many statements reach it.</p>
 */
public final class RecordScanner {

    private final Path sourceDirectory;
    private final JavaSourceParser parser;
    /**
     * Concurrent, because a run reaches this from parallel streams twice over — once while the
     * statements are configured, once while their repositories are generated. A plain map loses
     * entries or throws when two threads compute at the same time.
     */
    private final Map<ClassName, Optional<JavaSourceType>> scanned = new ConcurrentHashMap<>();

    public RecordScanner(final FilesConfiguration files, final JavaSourceParser parser) {
        this.sourceDirectory = files.sourceDirectory();
        this.parser = parser;
    }

    /**
     * @return the type's shape, or empty when no source file exists for it — which is the normal
     *         answer for a JDK type such as {@code java.util.UUID}.
     */
    public Optional<JavaSourceType> scan(final ClassName type) {
        return scanned.computeIfAbsent(type, this::read);
    }

    /**
     * @return where the source of {@code type} is expected, for a diagnostic to name.
     */
    public Path locationOf(final ClassName type) {
        final var topLevel = type.topLevelClassName();
        final var directory = topLevel.packageName().isEmpty()
                ? sourceDirectory
                : sourceDirectory.resolve(topLevel.packageName().replace('.', java.io.File.separatorChar));
        return directory.resolve(topLevel.simpleName() + ".java");
    }

    /**
     * Whether a name written without a package means a type of this project rather than one of the
     * JDK's. Only a file next to it can say so, which is why the parser asks rather than guesses.
     */
    private boolean hasSource(final String qualifiedName) {
        try {
            return Files.isRegularFile(locationOf(ClassName.bestGuess(qualifiedName)));
        } catch (final IllegalArgumentException _) {
            return false;
        }
    }

    private Optional<JavaSourceType> read(final ClassName type) {
        final var location = locationOf(type);
        if (!Files.isRegularFile(location)) {
            return Optional.empty();
        }
        try {
            return Optional.of(parser.parse(
                    Files.readString(location, StandardCharsets.UTF_8), location, type, this::hasSource));
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

}
