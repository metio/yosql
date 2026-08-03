/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.Action;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

public class GenerateTaskConfiguration implements Action<GenerateCodeTask> {

    private final YoSqlExtension extension;

    public GenerateTaskConfiguration(final YoSqlExtension extension) {
        this.extension = extension;
    }

    @Override
    public void execute(final GenerateCodeTask task) {
        task.getInputDirectory().set(extension.getFiles().getInputBaseDirectory());
        task.getOutputDirectory().set(extension.getFiles().getOutputBaseDirectory());
        task.getRuntimeConfiguration().set(RuntimeConfiguration.builder()
                .setAnnotations(extension.getAnnotations().asConfiguration())
                .setConverter(extension.getConverter().asConfiguration())
                .setFiles(extension.getFiles().asConfiguration())
                .setLogging(extension.getLogging().asConfiguration())
                .setRepositories(extension.getRepositories().asConfiguration())
                .setResources(extension.getResources().asConfiguration())
                .setSchema(extension.getSchema().asConfiguration())
                .build());
    }

}
