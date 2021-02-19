package wtf.metio.yosql.meta.config.data;

import wtf.metio.yosql.meta.config.ConfigurationGroup;

public final class Annotations {

    public static ConfigurationGroup annotations() {
        return ConfigurationGroup.builder()
                .build();
    }

    private Annotations() {
        // data class
    }

}
