/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
