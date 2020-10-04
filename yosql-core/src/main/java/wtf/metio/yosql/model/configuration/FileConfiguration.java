/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;

import java.nio.charset.Charset;
import java.nio.file.Path;

@Value.Immutable
public interface FileConfiguration {

    static ImmutableFileConfiguration.Builder builder() {
        return ImmutableFileConfiguration.builder();
    }

    /**
     * @return The base directory for SQL file parsing.
     */
    Path inputBaseDirectory();

    /**
     * @return The base directory for writing .java files.
     */
    Path outputBaseDirectory();

    /**
     * @return The SQL statement separator to use, e.g. ";".
     */
    String sqlStatementSeparator();

    /**
     * @return The charset to use while reading SQL files, e.g. "UTF-8".
     */
    Charset sqlFilesCharset();

    /**
     * @return The file suffix to use while searching for SQL files, e.g. ".sql".
     */
    String sqlFilesSuffix();

}
