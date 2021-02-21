package wtf.metio.yosql.internals.meta.model.data;

import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;

import java.util.List;

/**
 * Exposes all configuration groups/settings for convenient access.
 */
public final class AllConfigurations {

    public static List<ConfigurationGroup> allConfigurationGroups() {
        return List.of(Annotations.configurationGroup(),
                Api.configurationGroup(),
                Converters.configurationGroup(),
                Files.configurationGroup(),
                Java.configurationGroup());
    }

}
