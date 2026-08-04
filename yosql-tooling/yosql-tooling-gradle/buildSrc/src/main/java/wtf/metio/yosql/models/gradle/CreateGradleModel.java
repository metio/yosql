/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.gradle;

import com.palantir.javapoet.JavaFile;
import org.gradle.api.Action;
import org.gradle.api.Task;
import wtf.metio.yosql.model.generator.ModelGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreateGradleModel implements Action<Task> {

    private final ModelGenerator generator;
    private final Path outputDirectory;

    public CreateGradleModel(final ModelGenerator generator, final Path outputDirectory) {
        this.generator = generator;
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void execute(final Task task) {
        discardPreviousOutput();
        generator.createGradleModel((targetPackageName, typeSpec) -> {
            try {
                JavaFile.builder(targetPackageName, typeSpec)
                        .build()
                        .writeTo(outputDirectory);
            } catch (final IOException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    /**
     * Removes what the last run wrote, before this one writes anything.
     *
     * <p>The output directory is a source directory of this build and survives between runs, so a
     * configuration group removed from the meta-model left its class behind and kept compiling: the
     * build stayed green while a fresh checkout would not have. This is the counterpart of the same
     * step on the Maven side, and it matters more here, because Gradle is the one frontend
     * {@code mvn verify} never compiles.</p>
     *
     * <p>Only the files this writes, rather than the directory wholesale.</p>
     */
    private void discardPreviousOutput() {
        if (!Files.isDirectory(outputDirectory)) {
            return;
        }
        try (final var found = Files.walk(outputDirectory)) {
            final var stale = found.filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().endsWith(".java"))
                    .toList();
            for (final var file : stale) {
                Files.delete(file);
            }
        } catch (final IOException exception) {
            throw new RuntimeException("Cannot clear " + outputDirectory, exception);
        }
    }

}
