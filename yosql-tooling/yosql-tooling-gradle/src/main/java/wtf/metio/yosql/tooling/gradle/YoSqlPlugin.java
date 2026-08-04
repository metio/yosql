/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.compile.JavaCompile;

/**
 * The YoSQL Gradle plugin. It configures the {@link YoSqlExtension} and registers the {@link GenerateCodeTask}.
 */
public class YoSqlPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        final var extension = project.getExtensions().create("yosql", YoSqlExtension.class);
        configureConventions(extension, project.getLayout());
        configureSourceSets(project, registerTask(project, extension));
    }

    private static void configureConventions(
            final YoSqlExtension extension,
            final ProjectLayout layout) {
        extension.getAnnotations().configureConventions();
        extension.getConverter().configureConventions();
        extension.getFiles().configureConventions(layout);
        extension.getLogging().configureConventions();
        extension.getRepositories().configureConventions();
        extension.getResources().configureConventions();
        extension.getSchema().configureConventions();
    }

    private static TaskProvider<GenerateCodeTask> registerTask(
            final Project project, final YoSqlExtension extension) {
        final var generate = project.getTasks().register("generateJavaCode",
                GenerateCodeTask.class, new GenerateTaskConfiguration(extension,
                        project.getLayout().getProjectDirectory().getAsFile().toPath()));
        // A dependency, not a doFirst that reaches in and calls the task. Running it through the
        // task graph is what gives it up-to-date checks, build-cache entries, and a place in
        // `gradle tasks` and `--dry-run` — and it is why a project whose SQL has not changed no
        // longer regenerates on every build.
        project.getTasks().withType(JavaCompile.class, task -> task.dependsOn(generate));
        return generate;
    }

    /**
     * Adds what the generator writes to the sources the project compiles.
     *
     * <p>The task's own output directory, rather than the extension's property. This runs while the
     * plugin is being applied, which is before the build script's {@code yosql { }} block has said
     * anything — so reading a value here freezes the convention, and a project that moves
     * {@code outputBaseDirectory} would generate into one place and compile from another. Taking it
     * from the task keeps it a provider Gradle resolves later, and one it can name a producer for,
     * which is what lets it be an output directory and a source directory at once.</p>
     */
    private static void configureSourceSets(
            final Project project, final TaskProvider<GenerateCodeTask> generate) {
        project.getPlugins().withType(JavaPlugin.class, plugin -> project.getExtensions()
                .getByType(JavaPluginExtension.class)
                .getSourceSets()
                .getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                .getJava()
                .srcDir(generate.flatMap(GenerateCodeTask::getOutputDirectory)));
    }

}
