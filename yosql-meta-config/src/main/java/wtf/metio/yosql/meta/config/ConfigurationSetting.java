package wtf.metio.yosql.meta.config;

import com.squareup.javapoet.TypeName;
import org.immutables.value.Value;

import java.util.Optional;

@Value.Immutable
public interface ConfigurationSetting {

    //region builders

    static ImmutableConfigurationSetting.Builder builder() {
        return ImmutableConfigurationSetting.builder();
    }

    //endregion

    String name();
    TypeName type();
    String description();
    Optional<Object> defaultValue();

}
