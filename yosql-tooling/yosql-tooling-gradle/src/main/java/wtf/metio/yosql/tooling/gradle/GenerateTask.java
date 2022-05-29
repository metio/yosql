/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.DaggerYoSQLComponent;

/**
 * Generate Java code by reading SQL code.
 */
public abstract class GenerateTask extends DefaultTask {

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
        DaggerYoSQLComponent.builder()
                .runtimeConfiguration(getRuntimeConfiguration().get())
                .locale(SupportedLocales.defaultLocale())
                .build()
                .yosql()
                .generateCode();
    }

}
