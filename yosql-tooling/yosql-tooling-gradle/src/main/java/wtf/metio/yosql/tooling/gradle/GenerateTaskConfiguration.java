/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
