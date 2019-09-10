package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;
import com.squareup.javapoet.ClassName;

@AutoValue
public abstract class ResultConfiguration {

    public static Builder builder() {
        return new AutoValue_ResultConfiguration.Builder();
    }

    public abstract ClassName resultStateClass();

    public abstract ClassName resultRowClass();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setResultStateClass(ClassName resultStateClass);

        public abstract Builder setResultRowClass(ClassName resultRowClass);

        public abstract ResultConfiguration build();

    }

}
