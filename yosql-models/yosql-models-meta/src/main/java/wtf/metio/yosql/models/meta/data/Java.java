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

public final class Java extends AbstractConfigurationGroup {

    private static final String GROUP_NAME = Java.class.getSimpleName();

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(GROUP_NAME)
                .setDescription("Configures Java version and related settings.")
                .addSettings(apiVersion())
                .addSettings(useFinalParameters())
                .addSettings(useFinalVariables())
                .addSettings(useFinalClasses())
                .addSettings(useFinalFields())
                .addSettings(useFinalMethods())
                .addSettings(useTextBlocks())
                .addSettings(useVar())
                .addSettings(useSealedInterfaces())
                .addImmutableMethods(immutableBuilder(GROUP_NAME))
                .addImmutableMethods(immutableCopyOf(GROUP_NAME))
                .addImmutableAnnotations(immutableAnnotation())
                .build();
    }

    private static ConfigurationSetting apiVersion() {
        final var name = "apiVersion";
        final var description = "Controls the Java SDK API version to use in generated code.";
        final var value = 17;
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value of the `apiVersion` configuration option is `17`. It is updated alongside the minimum Java version required by `YoSQL`.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(11))
                        .setDescription("Changing the `apiVersion` configuration option to `11` will allow generated code to use Java APIs up until version 11 (including).")
                        .build())
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

    private static ConfigurationSetting useTextBlocks() {
        final var name = "useTextBlocks";
        final var description = "Controls the usage of text blocks in generated code.";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value of the `useTextBlocks` configuration option is `true` which enables the use of text blocks in generated code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(!value))
                        .setDescription("Changing the `useTextBlocks` configuration option to `false` disables the use of text blocks in generated code.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useVar() {
        final var name = "useVar";
        final var description = "Controls the usage of the 'var' keyword in generated code.";
        final var value = true;
        return setting(GROUP_NAME, name, description, value)
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(value))
                        .setDescription("The default value of the `useVar` configuration option is `true` which enables the use of `var` in generated code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue(String.valueOf(!value))
                        .setDescription("Changing the `useVar` configuration option to `false` disables the use of `var` in generated code.")
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
