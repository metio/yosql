/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.cli.options;

import picocli.CommandLine;
import wtf.metio.yosql.tooling.codegen.model.configuration.FileConfiguration;

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Configures how files are handled.
 */
public class Files {

    @CommandLine.Option(
            names = "--files-input-directory",
            description = "The input directory for the user written SQL files.",
            defaultValue = ".")
    Path inputBaseDirectory;

    @CommandLine.Option(
            names = "--files-output-directory",
            description = "The output directory for the generated classes.",
            defaultValue = ".")
    Path outputBaseDirectory;

    @CommandLine.Option(
            names = "--files-sql-suffix",
            description = "The file ending to use while searching for SQL files.",
            defaultValue = ".sql")
    String sqlFilesSuffix;

    @CommandLine.Option(
            names = "--files-sql-charset",
            description = "The charset to use while reading .sql files.",
            defaultValue = "UTF-8")
    Charset sqlFilesCharset;

    @CommandLine.Option(
            names = "--files-sql-separator",
            description = "The separator to split SQL statements inside a single .sql file.",
            defaultValue = ";")
    String sqlStatementSeparator;

    public FileConfiguration asConfiguration() {
        return FileConfiguration.builder()
                .setInputBaseDirectory(inputBaseDirectory)
                .setOutputBaseDirectory(outputBaseDirectory)
                .setSqlFilesSuffix(sqlFilesSuffix)
                .setSqlFilesCharset(sqlFilesCharset)
                .setSqlStatementSeparator(sqlStatementSeparator)
                .build();
    }

}
