/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.records;

import com.squareup.javapoet.ClassName;
import wtf.metio.yosql.models.immutables.FilesConfiguration;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
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
    private final Map<ClassName, Optional<JavaSourceType>> scanned = new HashMap<>();

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

    private Optional<JavaSourceType> read(final ClassName type) {
        final var location = locationOf(type);
        if (!Files.isRegularFile(location)) {
            return Optional.empty();
        }
        try {
            return Optional.of(parser.parse(Files.readString(location, StandardCharsets.UTF_8), location, type));
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

}
