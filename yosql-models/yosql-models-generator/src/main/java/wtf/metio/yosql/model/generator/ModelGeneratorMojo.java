/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiConsumer;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class ModelGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "immutables")
    String type;

    @Parameter(defaultValue = "target/generated-sources/yosql")
    String outputBaseDirectory;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;

    @Override
    public void execute() {
        final var modelGenerator = new ModelGenerator();
        final var outputDirectory = Paths.get(project.getBasedir().getAbsolutePath(), outputBaseDirectory);
        final var writer = typeWriter(outputDirectory);
        switch (type) {
            case "immutables" -> modelGenerator.createImmutableModel(writer);
            case "maven" -> modelGenerator.createMavenModel(writer);
            case "cli" -> modelGenerator.createCliModel(writer);
            case "ant" -> modelGenerator.createAntModel(writer);
            case "website" -> ModelGenerator.createMarkdownDocumentation(project.getVersion(), outputDirectory);
        }
    }

    private static BiConsumer<String, TypeSpec> typeWriter(final Path outputDirectory) {
        return (targetPackageName, typeSpec) -> {
            try {
                JavaFile.builder(targetPackageName, typeSpec)
                        .build()
                        .writeTo(outputDirectory);
            } catch (final IOException exception) {
                throw new RuntimeException(exception);
            }
        };
    }

}
