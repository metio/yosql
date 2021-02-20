package wtf.metio.yosql.meta.model.data;

import wtf.metio.yosql.meta.model.ConfigurationGroup;

public final class Api {

    public static ConfigurationGroup api() {
        return ConfigurationGroup.builder()
                .build();
    }

    private Api() {
        // data class
    }

}
