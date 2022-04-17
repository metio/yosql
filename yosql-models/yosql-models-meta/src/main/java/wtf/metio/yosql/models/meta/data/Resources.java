/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

public final class Resources {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(Resources.class.getSimpleName())
                .setDescription("Configures resource usage and other related settings")
                .addSettings(maxThreads())
                .build();
    }

    private static ConfigurationSetting maxThreads() {
        return ConfigurationSetting.builder()
                .setName("maxThreads")
                .setDescription("Controls how many threads are used during code generation.")
                .setType(TypeName.get(int.class))
                .setExplanation("The maximum number will be capped to the number of available CPU cores of your system.")
                .setValue(1)
                .addTags(Tags.THREADS)
                .addExamples(ConfigurationExample.builder()
                        .setValue("1")
                        .setDescription("The default value of the `maxThreads` configuration option is `1` which uses one thread to generate code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("123")
                        .setDescription("Changing the `maxThreads` configuration option to `123` will use the available number of CPU cores in your system or 123 threads, depending on which is lower.")
                        .build())
                .build();
    }

    private Resources() {
        // data class
    }

}
