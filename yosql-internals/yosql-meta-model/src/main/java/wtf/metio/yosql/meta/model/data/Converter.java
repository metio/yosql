package wtf.metio.yosql.meta.model.data;

import wtf.metio.yosql.meta.model.ConfigurationGroup;

public final class Converter {

    public static ConfigurationGroup converter() {
        return ConfigurationGroup.builder()
                .build();
    }

    private Converter() {
        // data class
    }

}
