/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.models.configuration.LoggingApis;
import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

public final class Logging extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Logging.class.getSimpleName();

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("Configures log statements in generated code.")
                .addSettings(api())
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableAnnotations(immutableAnnotation())
                .build();
    }

    private static ConfigurationSetting api() {
        final var name = "api";
        final var description = "The logging API to use.";
        final var type = TypeName.get(LoggingApis.class);
        final var value = LoggingApis.NONE;
        return enumSetting(GROUP_NAME, name, description, value, type)
                .addExamples(ConfigurationExample.builder()
                        .setValue(value.name())
                        .setDescription("The default `no-op` implementation for a logging generator. It won't generate any logging statements in your generated code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(LoggingApis.JUL.name())
                        .setDescription("The `java.util.logging` based implementation for a logging generator. The generated code does not require any external non-JDK classes. All loggers use the [basePackageName](/configuration/repositories/basepackagename/) as their base name.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(LoggingApis.LOG4J.name())
                        .setDescription("The [log4j](https://logging.apache.org/log4j/2.x/) based implementation for a logging generator. All loggers use the [basePackageName](/configuration/repositories/basepackagename/) as their base name.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(LoggingApis.SLF4J.name())
                        .setDescription("The [slf4j](https://www.slf4j.org/) based implementation for a logging generator. All loggers use the [basePackageName](/configuration/repositories/basepackagename/) as their base name.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(LoggingApis.SYSTEM.name())
                        .setDescription("The [System.Logger](https://docs.oracle.com/javase/9/docs/api/java/lang/System.Logger.html) based implementation for a logging generator.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(LoggingApis.TINYLOG.name())
                        .setDescription("The [Tinylog](https://tinylog.org/v2/) based implementation for a logging generator.")
                        .build())
                .build();
    }

    private Logging() {
        // data class
    }

}
