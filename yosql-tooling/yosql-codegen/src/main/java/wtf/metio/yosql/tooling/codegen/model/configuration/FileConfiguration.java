/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.model.configuration;

import org.immutables.value.Value;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configures the various file related options.
 */
@Value.Immutable
public interface FileConfiguration {

    static ImmutableFileConfiguration.Builder builder() {
        return ImmutableFileConfiguration.builder();
    }

    /**
     * @return A file configuration using default values.
     */
    static ImmutableFileConfiguration usingDefaults() {
        return builder().build();
    }

    /**
     * @return The base directory for SQL file parsing.
     */
    @Value.Default
    default Path inputBaseDirectory() {
        return Paths.get(".");
    }

    /**
     * @return The base directory for writing .java files.
     */
    @Value.Default
    default Path outputBaseDirectory() {
        return Paths.get(".");
    }

    /**
     * @return The SQL statement separator to use, e.g. ";".
     */
    @Value.Default
    default String sqlStatementSeparator() {
        return ";";
    }

    /**
     * @return The charset to use while reading SQL files, e.g. "UTF-8".
     */
    @Value.Default
    default Charset sqlFilesCharset() {
        return StandardCharsets.UTF_8;
    }

    /**
     * @return The file suffix to use while searching for SQL files, e.g. ".sql".
     */
    @Value.Default
    default String sqlFilesSuffix() {
        return ".sql";
    }

    /**
     * @return The number of lines to skip in each .sql file.
     */
    @Value.Default
    default Integer skipLines() {
        return 0;
    }

}
