package wtf.metio.yosql.internals.config.generator.settings;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.config.meta.ConfigurationGroup;
import wtf.metio.yosql.config.meta.ConfigurationSetting;

public final class Annotations {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(Annotations.class.getSimpleName())
                .addSettings(annotateClasses())
                .build();
    }

    private static ConfigurationSetting annotateClasses() {
        return ConfigurationSetting.builder()
                .setName("annotateClasses")
                .setDescription("Controls whether {@link Generated} annotations should be added to the generated classes.")
                .setType(TypeName.get(boolean.class))
                .setDefaultValue(false)
                .build();
    }

    private Annotations() {
        // factory class
    }

}
