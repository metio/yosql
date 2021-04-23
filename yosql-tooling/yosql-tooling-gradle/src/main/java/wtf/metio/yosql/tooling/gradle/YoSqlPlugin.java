/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import wtf.metio.yosql.models.immutables.FilesConfiguration;

/**
 * The YoSQL Gradle plugin. It configures the {@link YoSqlExtension} and registers the {@link GenerateTask}.
 */
public class YoSqlPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        final var extension = configureExtension(project);
        configureConventions(extension, project.getLayout());
        configureTask(project, extension);
        configureSourceSets(project, extension);
    }

    private YoSqlExtension configureExtension(final Project project) {
        return project.getExtensions().create("yosql", YoSqlExtension.class);
    }

    private void configureConventions(final YoSqlExtension extension, final ProjectLayout layout) {
        final var files = extension.getFiles();
        files.getInputBaseDirectory().convention(layout.getProjectDirectory().dir("src").dir("main").dir("yosql"));
        files.getOutputBaseDirectory().convention(layout.getBuildDirectory().dir("generated").map(d -> d.dir("sources").dir("yosql")));
    }

    private void configureTask(final Project project, final YoSqlExtension extension) {
        project.getTasks().register("generateJavaCode", GenerateTask.class, task ->
                task.getFiles().set(extension.getFiles().asConfiguration()));
    }

    private void configureSourceSets(final Project project, final YoSqlExtension extension) {
        project.getPlugins().withType(JavaPlugin.class, plugin -> addSourceSet(project, extension));
    }

    private void addSourceSet(final Project project, final YoSqlExtension extension) {
        project.getConvention()
                .getPlugin(JavaPluginConvention.class)
                .getSourceSets()
                .getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                .getAllSource()
                .srcDir(extension.getFiles().getOutputBaseDirectory());
    }

}
