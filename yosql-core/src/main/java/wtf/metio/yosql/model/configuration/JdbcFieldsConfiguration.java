package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class JdbcFieldsConfiguration {

    public static Builder builder() {
        return new AutoValue_JdbcFieldsOptions.Builder();
    }

    public abstract String rawSuffix();
    public abstract String indexSuffix();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setRawSuffix(String value);
        public abstract Builder setIndexSuffix(String value);

        public abstract JdbcFieldsConfiguration build();

    }

}
