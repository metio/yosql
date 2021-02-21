package wtf.metio.yosql.internals.config.generator.settings;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.config.meta.ConfigurationGroup;
import wtf.metio.yosql.config.meta.ConfigurationSetting;
import wtf.metio.yosql.config.meta.options.DaoApiOptions;

public final class Api {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(Api.class.getSimpleName())
                .addSettings(loggingApi())
                .build();
    }

    private static ConfigurationSetting loggingApi() {
        return ConfigurationSetting.builder()
                .setName("loggingApi")
                .setDescription("The logging API to use (default: <strong>auto</strong> which picks the logging API based on the projects dependencies). Possible other values are \"jdk\", \"log4j\", \"slf4j\" and \"none\".")
                .setType(TypeName.get(DaoApiOptions.class))
                .setDefaultValue(DaoApiOptions.JDBC)
                .build();
    }

    private Api() {
        // factory class
    }

}
