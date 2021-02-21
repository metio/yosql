package wtf.metio.yosql.internals.meta.model.data;

import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;

public final class Converter {

    public static ConfigurationGroup converter() {
        return ConfigurationGroup.builder()
                .build();
    }

    private Converter() {
        // data class
    }

}
