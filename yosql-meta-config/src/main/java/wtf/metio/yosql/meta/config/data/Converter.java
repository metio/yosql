package wtf.metio.yosql.meta.config.data;

import wtf.metio.yosql.meta.config.ConfigurationGroup;

public final class Converter {

    public static ConfigurationGroup converter() {
        return ConfigurationGroup.builder()
                .build();
    }

    private Converter() {
        // data class
    }

}
