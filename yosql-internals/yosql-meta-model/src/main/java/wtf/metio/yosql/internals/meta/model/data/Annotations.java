package wtf.metio.yosql.internals.meta.model.data;

import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;

public final class Annotations {

    public static ConfigurationGroup annotations() {
        return ConfigurationGroup.builder()
                .build();
    }

    private Annotations() {
        // data class
    }

}
