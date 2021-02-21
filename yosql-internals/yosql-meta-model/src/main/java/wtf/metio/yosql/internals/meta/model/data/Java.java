package wtf.metio.yosql.internals.meta.model.data;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;
import wtf.metio.yosql.internals.meta.model.ConfigurationSetting;

public final class Java {

    public static ConfigurationGroup java() {
        return ConfigurationGroup.builder()
                .setName("Java")
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
                .setDefaultValue(false)
                .setDescription("Should records be used")
                .build();
    }

    private static ConfigurationSetting useTextBlocks() {
        return ConfigurationSetting.builder()
                .setName("useTextBlocks")
                .setType(TypeName.get(boolean.class))
                .setDefaultValue(false)
                .setDescription("Should text blocks be used")
                .build();
    }

    private static ConfigurationSetting useVar() {
        return ConfigurationSetting.builder()
                .setName("useVar")
                .setType(TypeName.get(boolean.class))
                .setDefaultValue(false)
                .setDescription("Should variables use the 'var' keyword")
                .build();
    }

    private static ConfigurationSetting useProcessingApi() {
        return ConfigurationSetting.builder()
                .setName("useProcessingApi")
                .setType(TypeName.get(boolean.class))
                .setDefaultValue(true)
                .setDescription("Should the processing API be used")
                .build();
    }

    private static ConfigurationSetting useStreamAPI() {
        return ConfigurationSetting.builder()
                .setName("useStreamAPI")
                .setType(TypeName.get(boolean.class))
                .setDefaultValue(true)
                .setDescription("Should the stream API be used")
                .build();
    }

    private static ConfigurationSetting useDiamondOperator() {
        return ConfigurationSetting.builder()
                .setName("useDiamondOperator")
                .setType(TypeName.get(boolean.class))
                .setDefaultValue(true)
                .setDescription("Should the diamond operator be used")
                .build();
    }

    private static ConfigurationSetting useGenerics() {
        return ConfigurationSetting.builder()
                .setName("useGenerics")
                .setType(TypeName.get(boolean.class))
                .setDefaultValue(true)
                .setDescription("Should generic types be used")
                .build();
    }

    private static ConfigurationSetting useFinal() {
        return ConfigurationSetting.builder()
                .setName("useFinal")
                .setType(TypeName.get(boolean.class))
                .setDefaultValue(true)
                .setDescription("Should variables and parameters declared as final")
                .build();
    }

    private static ConfigurationSetting apiVersion() {
        return ConfigurationSetting.builder()
                .setName("apiVersion")
                .setType(TypeName.get(int.class))
                .setDefaultValue(15)
                .setDescription("The Java SDK API version to use.")
                .build();
    }

    private Java() {
        // data class
    }

}
