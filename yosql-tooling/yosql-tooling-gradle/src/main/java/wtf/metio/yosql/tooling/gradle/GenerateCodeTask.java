/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.DaggerYoSQLComponent;

/**
 * Generate Java code by reading SQL code.
 */
public abstract class GenerateCodeTask extends DefaultTask {

    /**
     * @return The runtime configuration to use.
     */
    @Input
    public abstract Property<RuntimeConfiguration> getRuntimeConfiguration();

    /**
     * Generate Java code.
     */
    @TaskAction
    public void generateCode() {
        try {
            DaggerYoSQLComponent.builder()
                    .runtimeConfiguration(getRuntimeConfiguration().get())
                    .locale(SupportedLocales.defaultLocale())
                    .build()
                    .yosql()
                    .generateCode();
        } catch (final Throwable throwable) {
            throw new GradleException("Failure to generate code", throwable);
        }

    }

}
