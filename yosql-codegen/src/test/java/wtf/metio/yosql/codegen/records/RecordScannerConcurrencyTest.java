/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.palantir.javapoet.ClassName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.models.immutables.FilesConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * One scanner, read from many threads.
 *
 * <p>A run reaches the scanner from two parallel streams — once while the statements are configured
 * and once while their repositories are generated — and Dagger hands out a single instance. So the
 * cache it keeps, and the parser behind it, are shared across threads whether or not anything says
 * so.</p>
 *
 * <p>Enough distinct types to make the map resize while it is being read, since that is when an
 * unsynchronised map loses entries rather than merely returning a stale one.</p>
 */
@DisplayName("a scanner shared between threads")
class RecordScannerConcurrencyTest {

    private static final int TYPES = 64;
    private static final int THREADS = 8;

    @Test
    @DisplayName("reads every record exactly as a single thread would")
    void scansConcurrently(@TempDir final Path directory) throws Exception {
        final var types = new ArrayList<ClassName>();
        for (var index = 0; index < TYPES; index++) {
            types.add(write(directory, index));
        }
        final var scanner = new RecordScanner(files(directory), new JavaSourceParser());

        final var tasks = new ArrayList<Callable<List<String>>>();
        for (var thread = 0; thread < THREADS; thread++) {
            tasks.add(() -> types.stream()
                    .map(type -> scanner.scan(type)
                            .map(source -> source.components().getFirst().name())
                            .orElse("<missing>"))
                    .toList());
        }

        final List<List<String>> answers;
        try (final var pool = Executors.newFixedThreadPool(THREADS)) {
            answers = pool.invokeAll(tasks).stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (final Exception exception) {
                            throw new IllegalStateException(exception);
                        }
                    })
                    .toList();
        }

        final var expected = types.stream()
                .map(type -> "component" + type.simpleName().substring("Record".length()))
                .toList();
        answers.forEach(answer -> Assertions.assertEquals(expected, answer,
                "every thread reads the same shape for the same type"));
    }

    private static FilesConfiguration files(final Path directory) {
        return FilesConfiguration.builder()
                .setSourceDirectory(directory)
                .setInputBaseDirectory(directory)
                .setOutputBaseDirectory(directory)
                .build();
    }

    private static ClassName write(final Path directory, final int index) throws IOException {
        final var name = "Record" + index;
        final var file = directory.resolve("com/example").resolve(name + ".java");
        Files.createDirectories(file.getParent());
        Files.writeString(file, """
                package com.example;

                public record %s(String component%d) {
                }
                """.formatted(name, index), StandardCharsets.UTF_8);
        return ClassName.get("com.example", name);
    }

}
