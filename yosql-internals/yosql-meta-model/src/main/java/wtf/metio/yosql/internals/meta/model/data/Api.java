package wtf.metio.yosql.internals.meta.model.data;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;
import wtf.metio.yosql.internals.meta.model.ConfigurationSetting;
import wtf.metio.yosql.internals.meta.model.options.DaoApiOptions;
import wtf.metio.yosql.internals.meta.model.options.LoggingApiOptions;

public final class Api {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(Api.class.getSimpleName())
                .setDescription("Configures which external APIs should be used.")
                .addSettings(daoApi())
                .addSettings(loggingApi())
                .build();
    }

    private static ConfigurationSetting daoApi() {
        return ConfigurationSetting.builder()
                .setName("daoApi")
                .setDescription("The DAO API to use (default: <strong>JDBC</strong>).")
                .setType(TypeName.get(DaoApiOptions.class))
                .setDefaultValue(DaoApiOptions.JDBC)
                .build();
    }

    private static ConfigurationSetting loggingApi() {
        return ConfigurationSetting.builder()
                .setName("loggingApi")
                .setDescription("The logging API to use (default: <strong>NONE</strong>).")
                .setType(TypeName.get(LoggingApiOptions.class))
                .setDefaultValue(LoggingApiOptions.NONE)
                .build();
    }

    private Api() {
        // data class
    }

}
