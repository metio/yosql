/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.Action;

public class GenerateTaskConfiguration implements Action<GenerateTask> {

    private final YoSqlExtension extension;

    public GenerateTaskConfiguration(final YoSqlExtension extension) {
        this.extension = extension;
    }

    @Override
    public void execute(final GenerateTask task) {
        task.getAnnotations().set(extension.getAnnotations().asConfiguration());
        task.getLoggingConfig().set(extension.getLogging().asConfiguration());
        task.getFiles().set(extension.getFiles().asConfiguration());
        task.getConverter().set(extension.getConverter().asConfiguration());
        task.getJava().set(extension.getJava().asConfiguration());
        task.getRepositories().set(extension.getRepositories().asConfiguration());
        task.getResources().set(extension.getResources().asConfiguration());
    }

}
