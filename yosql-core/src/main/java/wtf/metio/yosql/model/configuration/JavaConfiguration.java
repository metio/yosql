package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class JavaConfiguration {

    public static JavaConfiguration.Builder builder() {
        return new AutoValue_JavaConfiguration.Builder();
    }

    public abstract int targetVersion();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setTargetVersion(int targetVersion);

        public abstract JavaConfiguration build();

    }

}
