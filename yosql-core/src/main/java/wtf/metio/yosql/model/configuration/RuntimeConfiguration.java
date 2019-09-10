package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RuntimeConfiguration {

    public static Builder builder() {
        return new AutoValue_RuntimeConfiguration.Builder();
    }

    public abstract FileConfiguration files();

    public abstract JavaConfiguration java();

    public abstract LoggingConfiguration logging();

    public abstract MethodConfiguration methods();

    public abstract NameConfiguration names();

    public abstract ResourceConfiguration resources();

    public abstract RepositoryConfiguration repositories();

    public abstract StatementConfiguration statements();

    public abstract VariableConfiguration variables();

    public abstract JdbcNamesConfiguration jdbcNames();

    public abstract RxJavaConfiguration rxJava();

    public abstract ResultConfiguration result();

    public abstract AnnotationConfiguration annotations();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setFiles(FileConfiguration files);

        public abstract Builder setJava(JavaConfiguration java);

        public abstract Builder setLogging(LoggingConfiguration logging);

        public abstract Builder setMethods(MethodConfiguration methods);

        public abstract Builder setNames(NameConfiguration names);

        public abstract Builder setResources(ResourceConfiguration resources);

        public abstract Builder setRepositories(RepositoryConfiguration repository);

        public abstract Builder setStatements(StatementConfiguration statements);

        public abstract Builder setVariables(VariableConfiguration variables);

        public abstract Builder setJdbcNames(JdbcNamesConfiguration jdbcNames);

        public abstract Builder setRxJava(RxJavaConfiguration rxJava);

        public abstract Builder setResult(ResultConfiguration result);

        public abstract Builder setAnnotations(AnnotationConfiguration annotations);

        public abstract RuntimeConfiguration build();

    }

}
