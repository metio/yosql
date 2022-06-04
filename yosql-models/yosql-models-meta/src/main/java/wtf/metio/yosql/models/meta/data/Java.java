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

public final class Java {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName("Java")
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
                .build();
    }

    private static ConfigurationSetting apiVersion() {
        return ConfigurationSetting.builder()
                .setName("apiVersion")
                .setDescription("Controls the Java SDK API version to use in generated code.")
                .setType(TypeName.get(int.class))
                .setValue(17)
                .addExamples(ConfigurationExample.builder()
                        .setValue("17")
                        .setDescription("The default value of the `apiVersion` configuration option is `17`. It is updated alongside the minimum Java version required by `YoSQL`.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("11")
                        .setDescription("Changing the `apiVersion` configuration option to `11` will allow generated code to use Java APIs up until version 11 (including).")
                        .build())
                .build();
    }

    private static ConfigurationSetting useFinalParameters() {
        return ConfigurationSetting.builder()
                .setName("useFinalParameters")
                .setDescription("Controls whether parameters are declared as final in generated code.")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default value of the `useFinalParameters` configuration option is `true` which enables the use of `final` for parameters.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useFinalParameters` configuration option to `false` disables the use of `final` for parameters.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useFinalVariables() {
        return ConfigurationSetting.builder()
                .setName("useFinalVariables")
                .setDescription("Controls whether variables are declared as final in generated code.")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default value of the `useFinalVariables` configuration option is `true` which enables the use of `final` for variables.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useFinalVariables` configuration option to `false` disables the use of `final` for variables.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useFinalClasses() {
        return ConfigurationSetting.builder()
                .setName("useFinalClasses")
                .setDescription("Controls whether classes are declared as final in generated code.")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default value of the `useFinalClasses` configuration option is `true` which enables the use of `final` for classes.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useFinalClasses` configuration option to `false` disables the use of `final` for classes.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useFinalFields() {
        return ConfigurationSetting.builder()
                .setName("useFinalFields")
                .setDescription("Controls whether fields are declared as final in generated code.")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default value of the `useFinalFields` configuration option is `true` which enables the use of `final` for fields.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useFinalFields` configuration option to `false` disables the use of `final` for fields.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useFinalMethods() {
        return ConfigurationSetting.builder()
                .setName("useFinalMethods")
                .setDescription("Controls whether methods are declared as final in generated code.")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default value of the `useFinalMethods` configuration option is `true` which enables the use of `final` for methods.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useFinalMethods` configuration option to `false` disables the use of `final` for methods.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useRecords() {
        return ConfigurationSetting.builder()
                .setName("useRecords")
                .setDescription("Controls the usage of records in generated code.")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default value of the `useRecords` configuration option is `true` which enables the use of records in generated code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useRecords` configuration option to `false` disables the use of records in generated code.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useTextBlocks() {
        return ConfigurationSetting.builder()
                .setName("useTextBlocks")
                .setDescription("Controls the usage of text blocks in generated code.")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default value of the `useTextBlocks` configuration option is `true` which enables the use of text blocks in generated code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useTextBlocks` configuration option to `false` disables the use of text blocks in generated code.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useVar() {
        return ConfigurationSetting.builder()
                .setName("useVar")
                .setDescription("Controls the usage of the 'var' keyword in generated code.")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default value of the `useVar` configuration option is `true` which enables the use of `var` in generated code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useVar` configuration option to `false` disables the use of `var` in generated code.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useSealedInterfaces() {
        return ConfigurationSetting.builder()
                .setName("useSealedInterfaces")
                .setDescription("Controls whether interfaces should be sealed")
                .setType(TypeName.get(boolean.class))
                .setValue(false)
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("The default value of the `useSealedInterfaces` configuration option is `false` which disables sealing interfaces in generated code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useSealedInterfaces` configuration option to `true` enables sealing interfaces in generated code.")
                        .build())
                .build();
    }

    private Java() {
        // data class
    }

}
