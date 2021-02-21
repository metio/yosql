package wtf.metio.yosql.internals.meta.model.data;

import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;

public final class Converters {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(Converters.class.getSimpleName())
                .setDescription("Configures converters.")
                .build();
    }

    private Converters() {
        // data class
    }

}
