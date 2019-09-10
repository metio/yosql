package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RepositoryConfiguration {

    public static Builder builder() {
        return new AutoValue_RepositoryConfiguration.Builder();
    }

    public abstract String repositoryNameSuffix();

    public abstract boolean repositoryGenerateInterface();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setRepositoryNameSuffix(String repositoryNameSuffix);

        public abstract Builder setRepositoryGenerateInterface(boolean repositoryGenerateInterface);

        public abstract RepositoryConfiguration build();

    }

}
