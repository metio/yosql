package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ResourceConfiguration {

    public static ResourceConfiguration.Builder builder() {
        return new AutoValue_ResourceConfiguration.Builder();
    }

    /**
     * @return The maximum number of threads to use while working.
     */
    public abstract int maxThreads();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setMaxThreads(int maxThreads);

        public abstract ResourceConfiguration build();

    }

}
