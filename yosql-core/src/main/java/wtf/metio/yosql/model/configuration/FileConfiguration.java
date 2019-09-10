package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

import java.nio.file.Path;

@AutoValue
public abstract class FileConfiguration {

    public static Builder builder() {
        return new AutoValue_FileConfiguration.Builder();
    }

    /**
     * @return The base directory for SQL file parsing.
     */
    public abstract Path inputBaseDirectory();

    /**
     * @return The base directory for writing .java files.
     */
    public abstract Path outputBaseDirectory();

    public abstract String sqlStatementSeparator();

    public abstract String sqlFilesCharset();

    public abstract String sqlFilesSuffix();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setInputBaseDirectory(Path inputBaseDirectory);

        public abstract Builder setOutputBaseDirectory(Path outputBaseDirectory);

        public abstract Builder setSqlStatementSeparator(String value);

        public abstract Builder setSqlFilesCharset(String value);

        public abstract Builder setSqlFilesSuffix(String value);

        public abstract FileConfiguration build();

    }

}
