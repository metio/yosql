package wtf.metio.yosql.internals.meta.model.data;

import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;

public final class File {

    public static ConfigurationGroup file() {
        return ConfigurationGroup.builder()
                .build();
    }

    private File() {
        // data class
    }

}
