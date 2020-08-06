package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class JdbcNamesConfiguration {

    public static Builder builder() {
        return new AutoValue_JdbcNamesConfiguration.Builder();
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

        public abstract Builder setColumnCount(String columnCount);

        public abstract Builder setColumnLabel(String columnLabel);

        public abstract Builder setStatement(String statement);

        public abstract Builder setMetaData(String metaData);

        public abstract Builder setResultSet(String resultSet);

        public abstract Builder setBatch(String batch);

        public abstract Builder setList(String list);

        public abstract Builder setJdbcIndex(String jdbcIndex);

        public abstract Builder setIndex(String index);

        public abstract Builder setRow(String row);

        public abstract JdbcNamesConfiguration build();

    }

}
