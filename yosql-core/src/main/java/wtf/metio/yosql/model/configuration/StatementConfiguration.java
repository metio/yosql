package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;
import wtf.metio.yosql.model.options.StatementInRepositoryOptions;

@AutoValue
public abstract class StatementConfiguration {

    public static Builder builder() {
        return new AutoValue_StatementConfiguration.Builder();
    }

    // TODO: move to RepositoryConfig?
    public abstract StatementInRepositoryOptions embed();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setEmbed(StatementInRepositoryOptions targetVersion);

        public abstract StatementConfiguration build();

    }

}
