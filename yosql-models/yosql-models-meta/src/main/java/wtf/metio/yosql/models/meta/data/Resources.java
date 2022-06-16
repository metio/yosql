/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

public final class Resources extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Resources.class.getSimpleName();

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("Configures resource usage and other related settings")
                .addSettings(maxThreads())
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableAnnotations(immutableAnnotation())
                .build();
    }

    private static ConfigurationSetting maxThreads() {
        final var name = "maxThreads";
        final var description = "Controls how many threads are used during code generation.";
        final var value = 1;
        return setting(GROUP_NAME, name, description, value)
                .setExplanation("The maximum number will be capped to the number of available CPU cores of your system.")
                .addTags(Tags.THREADS)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value of the `maxThreads` configuration option is `1` which uses one thread to generate code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(123))
                        .setDescription("Changing the `maxThreads` configuration option to `123` will use the available number of CPU cores in your system or 123 threads, depending on which is lower.")
                        .build())
                .build();
    }

    private Resources() {
        // data class
    }

}
