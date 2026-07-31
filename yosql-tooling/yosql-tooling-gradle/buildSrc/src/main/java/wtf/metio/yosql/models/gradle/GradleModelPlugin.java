/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;
import wtf.metio.yosql.model.generator.ModelGenerator;

import java.nio.file.Path;

/**
 * Generates Java classes for the configuration model used by Gradle.
 */
public class GradleModelPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        final var outputDirectory = project.getBuildDir().toPath()
                .resolve("generated")
                .resolve("sources")
                .resolve("yosql");
        final var generator = new ModelGenerator();

        configureSourceSets(project, outputDirectory);
        registerTask(project, generator, outputDirectory);
    }

    private void registerTask(final Project project, final ModelGenerator generator, final Path outputDirectory) {
        project.getTasks().withType(JavaCompile.class, task -> task.doFirst("createGradleModel",
                new CreateGradleModel(generator, outputDirectory)));
    }

    private void configureSourceSets(final Project project, final Path outputDirectory) {
        project.getPlugins().withType(JavaPlugin.class, plugin -> project.getExtensions()
                .getByType(JavaPluginExtension.class)
                .getSourceSets()
                .getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                .getJava()
                .srcDir(outputDirectory));
    }

}
