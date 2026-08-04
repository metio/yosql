/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.Action;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import java.nio.file.Path;

public class GenerateTaskConfiguration implements Action<GenerateCodeTask> {

    private final YoSqlExtension extension;

    /**
     * What a relative directory in the configuration is relative to. Every frontend answers this
     * with the project rather than with whatever directory the build was started from.
     */
    private final Path projectDirectory;

    public GenerateTaskConfiguration(final YoSqlExtension extension, final Path projectDirectory) {
        this.extension = extension;
        this.projectDirectory = projectDirectory;
    }

    @Override
    public void execute(final GenerateCodeTask task) {
        task.getInputDirectory().set(extension.getFiles().getInputBaseDirectory());
        task.getOutputDirectory().set(extension.getFiles().getOutputBaseDirectory());
        // What the record scanner reads. Not a directory property, because it may well point
        // outside this project — the examples point it at a sibling module.
        task.getSourceDirectories().from(extension.getFiles().getSourceDirectory());
        task.getRuntimeConfiguration().set(RuntimeConfiguration.builder()
                .setAnnotations(extension.getAnnotations().asConfiguration())
                .setConverter(extension.getConverter().asConfiguration())
                .setFiles(extension.getFiles().asConfiguration())
                .setLogging(extension.getLogging().asConfiguration())
                .setRepositories(extension.getRepositories().asConfiguration())
                .setResources(extension.getResources().asConfiguration())
                .setSchema(extension.getSchema().asConfiguration(projectDirectory))
                .build());
    }

}
