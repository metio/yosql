/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.orchestration;

import ch.qos.cal10n.MessageConveyor;
import com.palantir.javapoet.TypeSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.codegen.dao.CodeGenerator;
import wtf.metio.yosql.codegen.files.FileParser;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.codegen.records.JavaSourceParser;
import wtf.metio.yosql.codegen.records.RecordScanner;
import wtf.metio.yosql.codegen.schema.SchemaValidator;
import wtf.metio.yosql.codegen.schema.Schemas;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SchemaConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import javax.lang.model.element.Modifier;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Two generated types that would be written to the same file.
 *
 * <p>A repository interface is the repository's name without its {@code Repository} suffix, so
 * statements in a {@code document} directory give an interface named {@code Document} — and a
 * {@code resultRowType} of {@code …persistence.Document} names the same type. Each name follows a
 * rule that is right on its own, and no generator sees both.</p>
 *
 * <p>What the collision used to produce was one file written twice, whichever ran last surviving:
 * the interface replaced the record, and the failure surfaced in {@code javac} on a generated
 * converter, saying the type it instantiates is abstract. Nothing in it named a statement, a
 * setting, or the two names that met.</p>
 */
@DisplayName("two generated types with one name")
final class DuplicateGeneratedTypesTest {

    @TempDir
    Path sources;

    @Test
    @DisplayName("fails the build before anything is written")
    void shouldRefuseToWriteEither() {
        final var written = new ArrayList<String>();

        final var thrown = assertThrows(RuntimeException.class,
                () -> yosql(written, collidingTypes()).generateCode());

        // Through the whole chain: a generator's complaint is collected and rethrown wrapped, and
        // what the build prints is the collected one rather than the wrapper.
        final var reported = everythingSaidBy(thrown);
        assertAll(
                () -> assertEquals(List.of(), written, "a file was written despite the collision"),
                () -> assertTrue(reported.contains("com.example.persistence.Document"), reported),
                () -> assertTrue(reported.contains("an interface and a record"), reported));
    }

    @Test
    @DisplayName("is only a collision when the package matches too")
    void shouldAllowTheSameNameInAnotherPackage() {
        final var written = new ArrayList<String>();

        yosql(written, distinctTypes()).generateCode();

        assertEquals(List.of("Document", "Document"), written);
    }

    private static String everythingSaidBy(final Throwable thrown) {
        final var said = new StringBuilder();
        for (var current = thrown; current != null; current = current.getCause()) {
            said.append(current.getMessage()).append('\n');
            for (final var suppressed : current.getSuppressed()) {
                said.append(everythingSaidBy(suppressed));
            }
        }
        return said.toString();
    }

    private static CodeGenerator collidingTypes() {
        return statements -> java.util.stream.Stream.of(
                PackagedTypeSpec.of(TypeSpec.interfaceBuilder("Document")
                        .addModifiers(Modifier.PUBLIC).build(), "com.example.persistence"),
                PackagedTypeSpec.of(TypeSpec.recordBuilder("Document")
                        .addModifiers(Modifier.PUBLIC).build(), "com.example.persistence"));
    }

    private static CodeGenerator distinctTypes() {
        return statements -> java.util.stream.Stream.of(
                PackagedTypeSpec.of(TypeSpec.interfaceBuilder("Document")
                        .addModifiers(Modifier.PUBLIC).build(), "com.example.persistence"),
                PackagedTypeSpec.of(TypeSpec.recordBuilder("Document")
                        .addModifiers(Modifier.PUBLIC).build(), "com.example.domain"));
    }

    private DefaultYoSQL yosql(final List<String> written, final CodeGenerator generator) {
        return new DefaultYoSQL(
                (FileParser) List::<SqlStatement>of,
                generator,
                (Executor) Runnable::run,
                new PassThroughTimer(),
                new MessageConveyor(SupportedLocales.ENGLISH),
                recording(written),
                OrchestrationObjectMother.executionErrors(),
                () -> {
                },
                new SchemaValidator(
                        Schemas.empty(),
                        SchemaConfiguration.builder().build(),
                        new RecordScanner(
                                FilesConfiguration.builder().setSourceDirectory(sources).build(),
                                new JavaSourceParser()),
                        LoggingObjectMother.logger()),
                new GeneratedTypeCollisions(new RecordScanner(
                        FilesConfiguration.builder().setSourceDirectory(sources).build(),
                        new JavaSourceParser())));
    }

    private static TypeWriter recording(final List<String> written) {
        return new TypeWriter() {
            @Override
            public void writeType(final PackagedTypeSpec typeSpec) {
                written.add(typeSpec.getType().name());
            }

            @Override
            public void removeStaleOutput() {
                // nothing on disk to remove
            }
        };
    }

    private static final class PassThroughTimer implements Timer {

        @Override
        public void timed(final String taskName, final Runnable task) {
            task.run();
        }

        @Override
        public <T> T timed(final String taskName, final Supplier<T> supplier) {
            return supplier.get();
        }

        @Override
        public void printTimings() {
            // nothing was timed
        }

    }

}
