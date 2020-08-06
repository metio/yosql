package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class JavaConfiguration {

    public static JavaConfiguration.Builder builder() {
        return new AutoValue_JavaConfiguration.Builder();
    }

    public abstract int targetVersion();

    public abstract boolean useVar();

    public abstract boolean useRecords();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setTargetVersion(int targetVersion);

        public abstract Builder setUseVar(boolean useVar);

        public abstract Builder setUseRecords(boolean useVar);

        public abstract JavaConfiguration build();

    }

}
