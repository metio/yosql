/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.gradle;

import com.squareup.javapoet.JavaFile;
import org.gradle.api.Action;
import org.gradle.api.Task;
import wtf.metio.yosql.model.generator.ModelGenerator;

import java.io.IOException;
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

}
