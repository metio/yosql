/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

public final class Java {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName("Java")
                .setDescription("Configures Java version and related settings.")
                .addSettings(apiVersion())
                .addSettings(useFinal())
                .addSettings(useGenerics())
                .addSettings(useDiamondOperator())
                .addSettings(useStreamAPI())
                .addSettings(useProcessingApi())
                .addSettings(useVar())
                .addSettings(useTextBlocks())
                .addSettings(useRecords())
                .build();
    }

    private static ConfigurationSetting useRecords() {
        return ConfigurationSetting.builder()
                .setName("useRecords")
                .setType(TypeName.get(boolean.class))
                .setValue(false)
                .setDescription("Should records be used")
                .build();
    }

    private static ConfigurationSetting useTextBlocks() {
        return ConfigurationSetting.builder()
                .setName("useTextBlocks")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Should text blocks be used")
                .build();
    }

    private static ConfigurationSetting useVar() {
        return ConfigurationSetting.builder()
                .setName("useVar")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Should variables use the 'var' keyword")
                .build();
    }

    private static ConfigurationSetting useProcessingApi() {
        return ConfigurationSetting.builder()
                .setName("useProcessingApi")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Should the processing API be used")
                .build();
    }

    private static ConfigurationSetting useStreamAPI() {
        return ConfigurationSetting.builder()
                .setName("useStreamAPI")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Should the stream API be used")
                .build();
    }

    private static ConfigurationSetting useDiamondOperator() {
        return ConfigurationSetting.builder()
                .setName("useDiamondOperator")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Should the diamond operator be used")
                .build();
    }

    private static ConfigurationSetting useGenerics() {
        return ConfigurationSetting.builder()
                .setName("useGenerics")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Should generic types be used")
                .build();
    }

    private static ConfigurationSetting useFinal() {
        return ConfigurationSetting.builder()
                .setName("useFinal")
                .setType(TypeName.get(boolean.class))
                .setValue(true)
                .setDescription("Should variables and parameters declared as final")
                .build();
    }

    private static ConfigurationSetting apiVersion() {
        return ConfigurationSetting.builder()
                .setName("apiVersion")
                .setType(TypeName.get(int.class))
                .setValue(15)
                .setDescription("The Java SDK API version to use.")
                .build();
    }

    private Java() {
        // data class
    }

}
