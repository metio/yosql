package wtf.metio.yosql.internals.meta.model;

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
    Optional<TypeName> mavenType();
    String description();
    Optional<Object> defaultValue();

}
