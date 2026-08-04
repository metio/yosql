/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.orchestration;

import com.palantir.javapoet.JavaFile;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.lifecycle.ApplicationErrors;
import wtf.metio.yosql.codegen.lifecycle.WriteLifecycle;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;

import wtf.metio.yosql.models.configuration.GeneratedNames;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultTypeWriter implements TypeWriter {

    private final LocLogger logger;
    private final FilesConfiguration fileConfiguration;
    private final ExecutionErrors errors;

    /**
     * What this run put on disk. Concurrent because the types are written from a parallel stream.
     */
    private final Set<Path> written = ConcurrentHashMap.newKeySet();

    /**
     * The text every generated file carries and nothing else does: the value {@code @Generated}
     * names the tool by.
     */
    private static final String MARKER = '"' + GeneratedNames.GENERATOR + '"';

    public DefaultTypeWriter(
            final LocLogger logger,
            final FilesConfiguration fileConfiguration,
            final ExecutionErrors errors) {
        this.logger = logger;
        this.fileConfiguration = fileConfiguration;
        this.errors = errors;
    }

    @Override
    public void writeType(final PackagedTypeSpec typeSpec) {
        try {
            JavaFile.builder(typeSpec.getPackageName(), typeSpec.getType())
                    .build()
                    .writeTo(fileConfiguration.outputBaseDirectory());
            written.add(locationOf(typeSpec));
            logger.debug(WriteLifecycle.FILE_WRITE_FINISHED,
                    fileConfiguration.outputBaseDirectory(),
                    typeSpec.getPackageName().replace(".", "/"),
                    typeSpec.getType().name());
        } catch (final IOException exception) {
            errors.add(exception);
            logger.error(ApplicationErrors.FILE_WRITE_FAILED,
                    typeSpec.getPackageName(),
                    typeSpec.getType().name(),
                    fileConfiguration.outputBaseDirectory());
        }
    }

    @Override
    public void removeStaleOutput() {
        final var output = fileConfiguration.outputBaseDirectory();
        if (!Files.isDirectory(output)) {
            return;
        }
        try (final var found = Files.walk(output)) {
            final var stale = found.filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().endsWith(".java"))
                    .filter(file -> !written.contains(file.toAbsolutePath().normalize()))
                    .filter(DefaultTypeWriter::wasGeneratedByUs)
                    .toList();
            for (final var file : stale) {
                Files.delete(file);
                logger.debug(WriteLifecycle.FILE_DELETE_FINISHED, file);
            }
        } catch (final IOException exception) {
            errors.add(exception);
        }
    }

    private Path locationOf(final PackagedTypeSpec typeSpec) {
        var location = fileConfiguration.outputBaseDirectory();
        for (final var part : typeSpec.getPackageName().split("\\.")) {
            if (!part.isEmpty()) {
                location = location.resolve(part);
            }
        }
        return location.resolve(typeSpec.getType().name() + ".java").toAbsolutePath().normalize();
    }

    /**
     * Whether the file says YoSQL wrote it. Read rather than assumed from where it sits: the output
     * directory is whatever the project pointed it at, and deleting somebody's own source because
     * they share a directory with the generator would be far worse than leaving a stale class.
     */
    private static boolean wasGeneratedByUs(final Path file) {
        try {
            return Files.readString(file, StandardCharsets.UTF_8).contains(MARKER);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

}
