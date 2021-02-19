package wtf.metio.yosql.meta.config.data;

import wtf.metio.yosql.meta.config.ConfigurationGroup;

public final class File {

    public static ConfigurationGroup file() {
        return ConfigurationGroup.builder()
                .build();
    }

    private File() {
        // data class
    }

}
