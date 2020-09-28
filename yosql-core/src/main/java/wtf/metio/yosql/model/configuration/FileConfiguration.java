/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

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
