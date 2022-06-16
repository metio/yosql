/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
