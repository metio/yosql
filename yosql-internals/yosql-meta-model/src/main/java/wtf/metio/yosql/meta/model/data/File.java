package wtf.metio.yosql.meta.model.data;

import wtf.metio.yosql.meta.model.ConfigurationGroup;

public final class File {

    public static ConfigurationGroup file() {
        return ConfigurationGroup.builder()
                .build();
    }

    private File() {
        // data class
    }

}
