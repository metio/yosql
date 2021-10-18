/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;
import wtf.metio.yosql.internals.model.generator.ModelGenerator;

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
        final var immutablesBasePackage = "wtf.metio.yosql.models.immutables";
        final var basePackage = "wtf.metio.yosql.tooling.gradle";
        final var generator = new ModelGenerator(outputDirectory, immutablesBasePackage, basePackage);

        configureSourceSets(project, outputDirectory);
        registerTask(project, generator);
    }

    private void registerTask(final Project project, final ModelGenerator generator) {
        project.getTasks().withType(JavaCompile.class, task -> task.doFirst("createGradleModel",
                action -> generator.createGradleModel()));
    }

    private void configureSourceSets(final Project project, final Path outputDirectory) {
        project.getPlugins().withType(JavaPlugin.class, plugin -> addSourceSet(project, outputDirectory));
    }

    private void addSourceSet(final Project project, final Path outputDirectory) {
        project.getConvention()
                .getPlugin(JavaPluginConvention.class)
                .getSourceSets()
                .getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                .getJava()
                .srcDir(outputDirectory);
    }

}