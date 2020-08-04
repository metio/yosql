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

        public abstract Builder setConnection(String connection);

        public abstract Builder setColumnCount(String connection);

        public abstract Builder setColumnLabel(String connection);

        public abstract Builder setStatement(String connection);

        public abstract Builder setMetaData(String connection);

        public abstract Builder setResultSet(String connection);

        public abstract Builder setBatch(String connection);

        public abstract Builder setList(String connection);

        public abstract Builder setJdbcIndex(String connection);

        public abstract Builder setIndex(String connection);

        public abstract Builder setRow(String connection);

        public abstract JdbcNamesConfiguration build();

    }

}
