package wtf.metio.yosql.internals.meta.model;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public interface ConfigurationGroup {

    //region builders

    static ImmutableConfigurationGroup.Builder builder() {
        return ImmutableConfigurationGroup.builder();
    }

    //endregion

    String name();
    List<ConfigurationSetting> settings();

}
