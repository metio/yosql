/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.model.generator;

import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.TypeSpec;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiConsumer;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class ModelGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "immutables")
    String type;

    @Parameter(defaultValue = "target/generated-sources/yosql")
    String outputBaseDirectory;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;

    /**
     * What this plugin writes, and therefore what it is allowed to remove again.
     */
    private static final List<String> GENERATED_SUFFIXES = List.of(".java", ".md", ".json");

    @Override
    public void execute() {
        final var modelGenerator = new ModelGenerator();
        final var outputDirectory = Paths.get(project.getBasedir().getAbsolutePath(), outputBaseDirectory);
        discardPreviousOutput(outputDirectory);
        final var writer = typeWriter(outputDirectory);
        switch (type) {
            case "immutables" -> modelGenerator.createImmutableModel(writer);
            case "maven" -> modelGenerator.createMavenModel(writer);
            case "cli" -> modelGenerator.createCliModel(writer);
            case "ant" -> modelGenerator.createAntModel(writer);
            case "website" -> ModelGenerator.createMarkdownDocumentation(project.getVersion(), outputDirectory);
            case "json-schema" -> ModelGenerator.createJsonSchema(project.getVersion(), outputDirectory);
        }
    }

    /**
     * Removes what an earlier run wrote, so that a setting deleted from the meta-model stops
     * existing here too.
     *
     * <p>Nothing else cleans this: `target` survives an incremental build, so the interface for a
     * removed configuration group stayed on disk and kept being compiled. The build stayed green
     * while a fresh checkout — every CI run, every other machine — would not have compiled at all.
     * Generating into a directory still holding the last run's answers is the whole problem, so the
     * last run's answers go first.</p>
     *
     * <p>Only the file kinds this plugin writes, rather than the directory wholesale: the output
     * directory is its own in every configuration today, and deleting by extension keeps that from
     * being a dangerous assumption if one day it is not.</p>
     */
    private void discardPreviousOutput(final Path outputDirectory) {
        if (!Files.isDirectory(outputDirectory)) {
            return;
        }
        try (final var found = Files.walk(outputDirectory)) {
            final var stale = found.filter(Files::isRegularFile)
                    .filter(ModelGeneratorMojo::isGenerated)
                    .toList();
            for (final var file : stale) {
                Files.delete(file);
            }
        } catch (final IOException exception) {
            throw new RuntimeException("Cannot clear " + outputDirectory, exception);
        }
    }

    private static boolean isGenerated(final Path file) {
        final var name = file.getFileName().toString();
        return GENERATED_SUFFIXES.stream().anyMatch(name::endsWith);
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
