/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;

/**
 * The YoSQL Gradle plugin. It configures the {@link YoSqlExtension} and registers the {@link GenerateTask}.
 */
public class YoSqlPlugin implements Plugin<Project> {

    @Override
    public void apply(final Project project) {
        final var extension = project.getExtensions().create("yosql", YoSqlExtension.class);
        project.getTasks().register("generateJavaCode", GenerateTask.class, task ->
                task.getFiles().set(extension.getFiles()));
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
