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
                .addSettings(useDiamondOperator())
                .addSettings(useFinal())
                .addSettings(useGenerics())
                .addSettings(useRecords())
                .addSettings(useStreamAPI())
                .addSettings(useTextBlocks())
                .addSettings(useVar())
                .build();
    }

    private static ConfigurationSetting apiVersion() {
        return ConfigurationSetting.builder()
                .setName("apiVersion")
                .setType(TypeName.get(int.class))
                .setValue(17)
                .setDescription("Controls the Java SDK API version to use in generated code.")
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

    private static ConfigurationSetting useDiamondOperator() {
        return ConfigurationSetting.builder()
                .setName("useDiamondOperator")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Controls whether the diamond operator is used in generated code.")
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default value of the `useDiamondOperator` configuration option is `true` which enables the use of the diamond operator in generated code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useDiamondOperator` configuration option to `false` disables the use of the diamond operator in generated code.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useFinal() {
        return ConfigurationSetting.builder()
                .setName("useFinal")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Controls whether variables and parameters are declared as final in generated code.")
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default value of the `useFinal` configuration option is `true` which enables the use of `final` for parameters and variables.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useFinal` configuration option to `false` disables the use of `final` for parameters and variables.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useGenerics() {
        return ConfigurationSetting.builder()
                .setName("useGenerics")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Controls the usage of generic types in generated code.")
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default value of the `useGenerics` configuration option is `true` which enables the use of generics in generated code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useGenerics` configuration option to `false` disables the use of generics in generated code.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useRecords() {
        return ConfigurationSetting.builder()
                .setName("useRecords")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Controls the usage of records in generated code.")
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

    private static ConfigurationSetting useStreamAPI() {
        return ConfigurationSetting.builder()
                .setName("useStreamAPI")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Controls the usage of the stream API in generated code.")
                .addExamples(ConfigurationExample.builder()
                        .setValue("true")
                        .setDescription("The default value of the `useStreamApi` configuration option is `true` which enables the use of the stream API in generated code.")
                        .build())
                .addExamples(ConfigurationExample.builder()
                        .setValue("false")
                        .setDescription("Changing the `useStreamApi` configuration option to `false` disables the use of the stream API in generated code.")
                        .build())
                .build();
    }

    private static ConfigurationSetting useTextBlocks() {
        return ConfigurationSetting.builder()
                .setName("useTextBlocks")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Controls the usage of text blocks in generated code.")
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
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Controls the usage of the 'var' keyword in generated code.")
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

    private Java() {
        // data class
    }

}
