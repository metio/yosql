/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.palantir.javapoet.ClassName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.models.immutables.FilesConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("RecordScanner")
class RecordScannerTest {

    @TempDir
    Path sources;

    private RecordScanner scanner() {
        return new RecordScanner(
                FilesConfiguration.builder().setSourceDirectory(sources).build(),
                new JavaSourceParser());
    }

    private void write(final String packageName, final String simpleName, final String body) throws IOException {
        final var directory = sources.resolve(packageName.replace('.', '/'));
        Files.createDirectories(directory);
        Files.writeString(directory.resolve(simpleName + ".java"), body);
    }

    @Test
    @DisplayName("finds a record at the path its package implies")
    void findsRecord() throws IOException {
        write("com.example.domain", "Tenant", """
                package com.example.domain;

                public record Tenant(String slug) {
                }
                """);
        final var found = scanner().scan(ClassName.get("com.example.domain", "Tenant")).orElseThrow();
        assertTrue(found.isRecord());
        assertEquals("slug", found.components().get(0).name());
    }

    @Test
    @DisplayName("finds a type in the default package")
    void findsRecordInDefaultPackage() throws IOException {
        write("", "Tenant", """
                public record Tenant(String slug) {
                }
                """);
        assertTrue(scanner().scan(ClassName.get("", "Tenant")).orElseThrow().isRecord());
    }

    @Test
    @DisplayName("answers empty for a type with no source, which is every JDK type")
    void missingSourceIsNotAnError() {
        assertTrue(scanner().scan(ClassName.get("java.util", "UUID")).isEmpty());
    }

    @Test
    @DisplayName("reads a file once however many types reach it")
    void caches() throws IOException {
        write("com.example.domain", "Money", """
                package com.example.domain;

                public record Money(long minorUnits) {
                }
                """);
        final var scanner = scanner();
        final var type = ClassName.get("com.example.domain", "Money");
        assertSame(scanner.scan(type).orElseThrow(), scanner.scan(type).orElseThrow());
    }

    @Test
    @DisplayName("remembers that a type has no source, too")
    void cachesAbsence() {
        final var scanner = scanner();
        final var type = ClassName.get("java.time", "Instant");
        assertTrue(scanner.scan(type).isEmpty());
        assertTrue(scanner.scan(type).isEmpty());
    }

    @Test
    @DisplayName("looks for a nested record in its top-level file")
    void nestedTypesLiveInTheirOuterFile() throws IOException {
        write("com.example.domain", "Holder", """
                package com.example.domain;

                public final class Holder {
                    public record Money(long minorUnits) {
                    }
                }
                """);
        final var nested = ClassName.get("com.example.domain", "Holder", "Money");
        assertEquals(sources.resolve("com/example/domain/Holder.java"), scanner().locationOf(nested));
        assertTrue(scanner().scan(nested).orElseThrow().isRecord());
    }

    @Test
    @DisplayName("names the file it looked in")
    void locationOf() {
        assertEquals(sources.resolve("com/example/domain/Tenant.java"),
                scanner().locationOf(ClassName.get("com.example.domain", "Tenant")));
    }

}
