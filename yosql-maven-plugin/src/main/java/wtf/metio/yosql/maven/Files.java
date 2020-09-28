/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.FileConfiguration;

import java.io.File;

/**
 * Configures how files are handled.
 */
public class Files {

    /**
     * The input directory for the user written SQL files (default: <strong>${project.baseDir}/src/main/yosql</strong>).
     */
    @Parameter(required = true, defaultValue = "${project.baseDir}/src/main/yosql")
    private File inputBaseDirectory;

    /**
     * The output directory for the generated classes (default:
     * <strong>${project.build.directory}/generated-sources/yosql</strong>).
     */
    @Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/yosql")
    private File outputBaseDirectory;

    /**
     * The file ending to use while searching for SQL files (default: <strong>.sql</strong>).
     */
    @Parameter(required = true, defaultValue = ".sql")
    private String sqlFilesSuffix;

    /**
     * The charset to use while reading .sql files (default: <strong>UTF-8</strong>).
     */
    @Parameter(required = true, defaultValue = "UTF-8")
    private String sqlFilesCharset;

    /**
     * The separator to split SQL statements inside a single .sql file (default: <strong>";"</strong>).
     */
    @Parameter(required = true, defaultValue = ";")
    private String sqlStatementSeparator;

    public FileConfiguration asConfiguration() {
        return FileConfiguration.builder()
                .setInputBaseDirectory(inputBaseDirectory.toPath())
                .setOutputBaseDirectory(outputBaseDirectory.toPath())
                .setSqlFilesSuffix(sqlFilesSuffix)
                .setSqlFilesCharset(sqlFilesCharset)
                .setSqlStatementSeparator(sqlStatementSeparator)
                .build();
    }

}
