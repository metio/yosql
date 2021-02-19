package wtf.metio.yosql.meta.config.data;

import wtf.metio.yosql.meta.config.ConfigurationGroup;

public final class Api {

    public static ConfigurationGroup api() {
        return ConfigurationGroup.builder()
                .build();
    }

    private Api() {
        // data class
    }

}
