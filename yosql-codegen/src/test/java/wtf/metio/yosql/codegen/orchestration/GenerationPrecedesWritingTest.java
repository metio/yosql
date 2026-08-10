/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.orchestration;

import com.palantir.javapoet.TypeSpec;
import ch.qos.cal10n.MessageConveyor;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The generator answers lazily, so nothing forces it to have finished before the writer starts —
 * and for a while nothing did: every repository was built while the files were being written. That
 * put the whole cost of generating under the writing stage's name in the timings, and it decided
 * when a generator's complaint was recorded, which was after the first files were already on disk.
 */
@DisplayName("the pipeline")
final class GenerationPrecedesWritingTest {

    private static final int TYPES = 200;

    @TempDir
    Path sources;

    @Test
    @DisplayName("finishes generating before it writes anything")
    void shouldFinishGeneratingBeforeWriting() {
        // Synchronized because the writing stage is parallel, which is also why the order the two
        // types are written in is not something to assert on.
        final var events = java.util.Collections.synchronizedList(new ArrayList<String>());
        final var yosql = new DefaultYoSQL(
                (FileParser) List::<SqlStatement>of,
                generatorRecording(events),
                (Executor) Runnable::run,
                new NoopTimer(),
                new MessageConveyor(SupportedLocales.ENGLISH),
                writerRecording(events),
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

        yosql.generateCode();

        final var lastGenerated = lastIndexOf(events, "generated:");
        final var firstWritten = firstIndexOf(events, "wrote:");
        assertAll(
                () -> assertEquals(2 * TYPES, events.size(), () -> String.valueOf(events.size())),
                () -> assertTrue(lastGenerated < firstWritten,
                        () -> ("every type has to be generated before the first is written, or the "
                                + "timings attribute generating to writing and a generator's "
                                + "complaint lands after files are already on disk; %d were still "
                                + "ungenerated when writing began")
                                .formatted(TYPES - firstWritten)));
    }

    private static int firstIndexOf(final List<String> events, final String prefix) {
        for (var index = 0; index < events.size(); index++) {
            if (events.get(index).startsWith(prefix)) {
                return index;
            }
        }
        return -1;
    }

    private static int lastIndexOf(final List<String> events, final String prefix) {
        for (var index = events.size() - 1; index >= 0; index--) {
            if (events.get(index).startsWith(prefix)) {
                return index;
            }
        }
        return -1;
    }

    private static CodeGenerator generatorRecording(final List<String> events) {
        // Enough of them that a lazy stream cannot help but interleave: with two, the parallel
        // spliterator splits them and evaluates both before either is consumed, so the difference
        // this test is about does not show.
        return statements -> java.util.stream.IntStream.range(0, TYPES)
                .mapToObj(index -> "Type" + index)
                .map(name -> {
                    events.add("generated:" + name);
                    return PackagedTypeSpec.of(
                            TypeSpec.classBuilder(name).addModifiers(Modifier.PUBLIC).build(),
                            "com.example.persistence");
                });
    }

    private static TypeWriter writerRecording(final List<String> events) {
        return new TypeWriter() {
            @Override
            public void writeType(final PackagedTypeSpec typeSpec) {
                events.add("wrote:" + typeSpec.getType().name());
            }

            @Override
            public void removeStaleOutput() {
                // nothing on disk to remove
            }
        };
    }

    /**
     * Times nothing, so that what is measured here is the order of the stages rather than their cost.
     */
    private static final class NoopTimer implements Timer {

        @Override
        public void timed(final String taskName, final Runnable task) {
            task.run();
        }

        @Override
        public <T> T timed(final String taskName, final java.util.function.Supplier<T> supplier) {
            return supplier.get();
        }

        @Override
        public void printTimings() {
            // nothing was timed
        }

    }

}
