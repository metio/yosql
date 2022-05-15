/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;

/**
 * The YoSQL Gradle plugin. It configures the {@link YoSqlExtension} and registers the {@link GenerateTask}.
 */
public class YoSqlPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        final var extension = createExtension(project);
        configureConventions(extension, project.getLayout(), project.getObjects());
        registerTask(project, extension);
        configureSourceSets(project, extension);
    }

    private YoSqlExtension createExtension(final Project project) {
        return project.getExtensions().create("yosql", YoSqlExtension.class);
    }

    private void configureConventions(final YoSqlExtension extension, final ProjectLayout layout, final ObjectFactory objects) {
        extension.getFiles().configureConventions(layout);
        extension.getConverter().configureConventions(objects);
        extension.getAnnotations().configureConventions();
        extension.getLogging().configureConventions();
        extension.getJava().configureConventions();
        extension.getRepositories().configureConventions();
        extension.getResources().configureConventions();
    }

    private void registerTask(final Project project, final YoSqlExtension extension) {
        final var generate = project.getTasks().register("generateJavaCode",
                GenerateTask.class, new GenerateTaskConfiguration(extension));
        project.getTasks().withType(JavaCompile.class, task -> task.doFirst("yosql",
                new GenerateCodeAction(generate)));
    }

    private void configureSourceSets(final Project project, final YoSqlExtension extension) {
        project.getPlugins().withType(JavaPlugin.class, plugin -> project.getExtensions()
                .getByType(JavaPluginExtension.class)
                .getSourceSets()
                .getByName(SourceSet.MAIN_SOURCE_SET_NAME)
                .getJava()
                .srcDir(extension.getFiles().getOutputBaseDirectory().get()));
    }

}
