/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.orchestration;

import com.palantir.javapoet.TypeSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.codegen.exceptions.DuplicateGeneratedTypeException;
import wtf.metio.yosql.codegen.exceptions.GeneratedTypeExistsException;
import wtf.metio.yosql.codegen.records.JavaSourceParser;
import wtf.metio.yosql.codegen.records.RecordScanner;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Two types that would end up as one file, from either direction.
 *
 * <p>A repository interface is the repository's name without its {@code Repository} suffix, so
 * statements in a {@code windDown} directory generate an interface called {@code WindDown}. That
 * name can meet a second generated type, and it can meet a class the project already has — and the
 * second is the ordinary shape, because a project whose stores live in the repositories' package
 * has hand-written classes sitting exactly where the generator writes.</p>
 */
@DisplayName("a generated type that is not the only one with its name")
final class GeneratedTypeCollisionsTest {

    private static final String PACKAGE = "com.example.store";

    @TempDir
    Path sources;

    private GeneratedTypeCollisions collisions() {
        return new GeneratedTypeCollisions(new RecordScanner(
                FilesConfiguration.builder().setSourceDirectory(sources).build(),
                new JavaSourceParser()));
    }

    private static PackagedTypeSpec anInterface(final String name) {
        return PackagedTypeSpec.of(
                TypeSpec.interfaceBuilder(name).addModifiers(Modifier.PUBLIC).build(), PACKAGE);
    }

    private static PackagedTypeSpec aRecord(final String name) {
        return PackagedTypeSpec.of(
                TypeSpec.recordBuilder(name).addModifiers(Modifier.PUBLIC).build(), PACKAGE);
    }

    private void existingSource(final String name, final String body) throws IOException {
        final var directory = sources.resolve(PACKAGE.replace('.', '/'));
        Files.createDirectories(directory);
        Files.writeString(directory.resolve(name + ".java"), body, StandardCharsets.UTF_8);
    }

    @Nested
    @DisplayName("against another generated type")
    class AgainstGenerated {

        @Test
        @DisplayName("names both kinds")
        void shouldRefuseTwoGeneratedTypes() {
            final var thrown = assertThrows(DuplicateGeneratedTypeException.class, () ->
                    collisions().reject(List.of(anInterface("Document"), aRecord("Document"))));

            assertAll(
                    () -> assertTrue(thrown.getMessage().contains("com.example.store.Document"),
                            thrown.getMessage()),
                    () -> assertTrue(thrown.getMessage().contains("an interface and a record"),
                            thrown.getMessage()));
        }

        @Test
        @DisplayName("is content with one of each name")
        void shouldAllowDistinctNames() {
            assertDoesNotThrow(() ->
                    collisions().reject(List.of(anInterface("Document"), aRecord("Payload"))));
        }

    }

    @Nested
    @DisplayName("against a class the project already has")
    class AgainstExistingSource {

        @Test
        @DisplayName("names the file it would have collided with")
        void shouldRefuseAHandWrittenClass() throws IOException {
            // What a project hits with generateInterfaces on and its stores in the repositories'
            // package: generation succeeded, and javac reported a duplicate class in a generated
            // file, naming neither the statements directory nor the class it met.
            existingSource("WindDown", """
                    package com.example.store;

                    public final class WindDown {
                    }
                    """);

            final var thrown = assertThrows(GeneratedTypeExistsException.class, () ->
                    collisions().reject(List.of(anInterface("WindDown"))));

            assertAll(
                    () -> assertTrue(thrown.getMessage().contains("com.example.store.WindDown"),
                            thrown.getMessage()),
                    () -> assertTrue(thrown.getMessage().contains("WindDown.java"),
                            thrown.getMessage()),
                    () -> assertTrue(thrown.getMessage().contains("generateInterfaces"),
                            thrown.getMessage()));
        }

        @Test
        @DisplayName("says nothing about a class of another name")
        void shouldAllowUnrelatedSources() throws IOException {
            existingSource("WindDown", """
                    package com.example.store;

                    public final class WindDown {
                    }
                    """);

            assertDoesNotThrow(() -> collisions().reject(List.of(anInterface("Document"))));
        }

        @Test
        @DisplayName("passes over an answer of its own from an earlier run")
        void shouldIgnoreItsOwnOutput() throws IOException {
            // A project is free to point sourceDirectory at somewhere holding generated code. What
            // is there is the file this run is about to replace, and failing for its existence would
            // make the second build of an unchanged project fail.
            existingSource("Document", """
                    package com.example.store;

                    @javax.annotation.processing.Generated(
                        value = "wtf.metio.yosql",
                        comments = "generated by YoSQL - do not modify"
                    )
                    public interface Document {
                    }
                    """);

            assertDoesNotThrow(() -> collisions().reject(List.of(anInterface("Document"))));
        }

    }

}
