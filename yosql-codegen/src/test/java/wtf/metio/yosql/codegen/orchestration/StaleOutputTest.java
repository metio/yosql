/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.orchestration;

import com.palantir.javapoet.TypeSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.models.configuration.GeneratedNames;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * What a run leaves behind is what it wrote.
 *
 * <p>The output directory survives a build, so a repository whose statement has been deleted or
 * renamed stayed on disk and kept compiling. Nothing could notice: the build was green, and only a
 * fresh checkout would have shown the class was never generated at all.</p>
 */
@DisplayName("output of an earlier run")
class StaleOutputTest {

    @TempDir
    Path output;

    private DefaultTypeWriter writer() {
        return new DefaultTypeWriter(
                LoggingObjectMother.logger(),
                FilesConfiguration.builder().setOutputBaseDirectory(output).build(),
                OrchestrationObjectMother.executionErrors());
    }

    private static PackagedTypeSpec type(final String name) {
        return PackagedTypeSpec.of(TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(com.palantir.javapoet.AnnotationSpec
                        .builder(com.palantir.javapoet.ClassName.bestGuess("javax.annotation.processing.Generated"))
                        .addMember("value", "$S", GeneratedNames.GENERATOR)
                        .build())
                .build(), "com.example.persistence");
    }

    private Path fileFor(final String name) {
        return output.resolve("com/example/persistence").resolve(name + ".java");
    }

    @Test
    @DisplayName("is removed when this run no longer writes it")
    void removesWhatIsNoLongerGenerated() throws IOException {
        final var first = writer();
        first.writeType(type("TenantRepository"));
        first.writeType(type("AccountRepository"));
        first.removeStaleOutput();
        Assertions.assertTrue(Files.isRegularFile(fileFor("AccountRepository")));

        // The second run stands for a project whose accountRepository statements were deleted.
        final var second = writer();
        second.writeType(type("TenantRepository"));
        second.removeStaleOutput();

        Assertions.assertAll(
                () -> Assertions.assertTrue(Files.isRegularFile(fileFor("TenantRepository")),
                        "what this run wrote stays"),
                () -> Assertions.assertFalse(Files.exists(fileFor("AccountRepository")),
                        "a repository nothing generates any more would otherwise keep compiling"));
    }

    @Test
    @DisplayName("is left alone when it is not ours")
    void keepsWhatSomebodyElseWrote() throws IOException {
        final var handWritten = fileFor("HandWritten");
        Files.createDirectories(handWritten.getParent());
        Files.writeString(handWritten, """
                package com.example.persistence;

                public class HandWritten {
                }
                """, StandardCharsets.UTF_8);

        final var writer = writer();
        writer.writeType(type("TenantRepository"));
        writer.removeStaleOutput();

        Assertions.assertTrue(Files.isRegularFile(handWritten),
                "the output directory is whatever the project pointed it at, and deleting a file "
                        + "somebody wrote there would be far worse than leaving a stale one");
    }

}
