/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
        task.getRuntimeConfiguration().set(RuntimeConfiguration.builder()
                .setAnnotations(extension.getAnnotations().asConfiguration())
                .setConverter(extension.getConverter().asConfiguration())
                .setFiles(extension.getFiles().asConfiguration())
                .setJava(extension.getJava().asConfiguration())
                .setLogging(extension.getLogging().asConfiguration())
                .setNames(extension.getNames().asConfiguration())
                .setRepositories(extension.getRepositories().asConfiguration())
                .setResources(extension.getResources().asConfiguration())
                .build());
    }

}
