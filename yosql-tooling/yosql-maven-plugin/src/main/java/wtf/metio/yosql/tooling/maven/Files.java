/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.maven;

import wtf.metio.yosql.tooling.codegen.model.configuration.FileConfiguration;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Configures how files are handled.
 */
@Deprecated(forRemoval = true) // Use class w/ same name from modals-maven package.
public class Files {

    /**
     * The input directory for the user written SQL files (default: <strong>${project.baseDir}/src/main/yosql</strong>).
     */
    private final String inputBaseDirectory = "src/main/yosql";

    /**
     * The output directory for the generated classes (default: <strong>generated-sources/yosql</strong>).
     */
    private final String outputBaseDirectory = "generated-sources/yosql";

    /**
     * The file ending to use while searching for SQL files (default: <strong>.sql</strong>).
     */
    private final String sqlFilesSuffix = ".sql";

    /**
     * The charset to use while reading .sql files (default: <strong>UTF-8</strong>).
     */
    private final String sqlFilesCharset = "UTF-8";

    /**
     * The separator to split SQL statements inside a single .sql file (default: <strong>";"</strong>).
     */
    private final String sqlStatementSeparator = ";";

    /**
     * The number of lines to skip in each file (e.g. a copyright header) (default: <strong>6</strong>).
     */
    private final Integer skipLines = 0;

    public FileConfiguration asConfiguration(final Path baseDirectory, final Path buildDirectory) {
        return FileConfiguration.builder()
                .setInputBaseDirectory(baseDirectory.resolve(inputBaseDirectory))
                .setOutputBaseDirectory(buildDirectory.resolve(outputBaseDirectory))
                .setSqlFilesSuffix(sqlFilesSuffix)
                .setSqlFilesCharset(Charset.forName(sqlFilesCharset))
                .setSqlStatementSeparator(sqlStatementSeparator)
                .setSkipLines(skipLines)
                .build();
    }

}
