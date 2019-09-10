package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class JdbcNamesConfiguration {

    public static Builder builder() {
        return new AutoValue_JdbcNameOptions.Builder();
    }

    public abstract String dataSource();

    public abstract String connection();

    public abstract String columnCount();

    public abstract String columnLabel();

    public abstract String statement();

    public abstract String metaData();

    public abstract String resultSet();

    public abstract String batch();

    public abstract String list();

    public abstract String jdbcIndex();

    public abstract String index();

    public abstract String row();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setDataSource(String dataSource);

        public abstract JdbcNamesConfiguration build();

    }

}
