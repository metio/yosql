package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;
import wtf.metio.yosql.model.options.VariableTypeOptions;

import javax.lang.model.element.Modifier;
import java.util.List;

@AutoValue
public abstract class VariableConfiguration {

    public static Builder builder() {
        return new AutoValue_VariableConfiguration.Builder();
    }

    public abstract VariableTypeOptions variableType();
    public abstract List<Modifier> modifiers();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setVariableType(VariableTypeOptions variableType);
        public abstract Builder setModifiers(List<Modifier> modifiers);

        public abstract VariableConfiguration build();

    }

}
