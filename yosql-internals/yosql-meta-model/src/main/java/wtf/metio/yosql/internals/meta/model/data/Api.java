package wtf.metio.yosql.internals.meta.model.data;

import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;

public final class Api {

    public static ConfigurationGroup api() {
        return ConfigurationGroup.builder()
                .build();
    }

    private Api() {
        // data class
    }

}
