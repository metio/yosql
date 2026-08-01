/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta.data;

import wtf.metio.yosql.models.meta.ConfigurationExample;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

public final class Java extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Java.class.getSimpleName();

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("Configures Java version and related settings.")
                .addSettings(useFinalParameters())
                .addSettings(useFinalVariables())
                .addSettings(useFinalClasses())
                .addSettings(useFinalFields())
                .addSettings(useFinalMethods())
                .addSettings(useSealedInterfaces())
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableAnnotations(immutableAnnotation())
                .build();
    }

    private static ConfigurationSetting useFinalParameters() {
        final var name = "useFinalParameters";
        final var description = "Controls whether parameters are declared as final in generated code.";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value of the `useFinalParameters` configuration option is `true` which enables the use of `final` for parameters.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(!value))
                        .setDescription("Changing the `useFinalParameters` configuration option to `false` disables the use of `final` for parameters.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useFinalVariables() {
        final var name = "useFinalVariables";
        final var description = "Controls whether variables are declared as final in generated code.";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value of the `useFinalVariables` configuration option is `true` which enables the use of `final` for variables.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(!value))
                        .setDescription("Changing the `useFinalVariables` configuration option to `false` disables the use of `final` for variables.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useFinalClasses() {
        final var name = "useFinalClasses";
        final var description = "Controls whether classes are declared as final in generated code.";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value of the `useFinalClasses` configuration option is `true` which enables the use of `final` for classes.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(!value))
                        .setDescription("Changing the `useFinalClasses` configuration option to `false` disables the use of `final` for classes.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useFinalFields() {
        final var name = "useFinalFields";
        final var description = "Controls whether fields are declared as final in generated code.";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value of the `useFinalFields` configuration option is `true` which enables the use of `final` for fields.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(!value))
                        .setDescription("Changing the `useFinalFields` configuration option to `false` disables the use of `final` for fields.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useFinalMethods() {
        final var name = "useFinalMethods";
        final var description = "Controls whether methods are declared as final in generated code.";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value of the `useFinalMethods` configuration option is `true` which enables the use of `final` for methods.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(!value))
                        .setDescription("Changing the `useFinalMethods` configuration option to `false` disables the use of `final` for methods.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useSealedInterfaces() {
        final var name = "useSealedInterfaces";
        final var description = "Controls whether interfaces should be sealed";
        final var value = false;
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value of the `useSealedInterfaces` configuration option is `false` which disables sealing interfaces in generated code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(!value))
                        .setDescription("Changing the `useSealedInterfaces` configuration option to `true` enables sealing interfaces in generated code.")
                        .build())
                .build();
    }

    private Java() {
        // data class
    }

}
