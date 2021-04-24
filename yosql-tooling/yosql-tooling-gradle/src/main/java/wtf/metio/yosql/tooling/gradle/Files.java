/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import wtf.metio.yosql.models.immutables.FilesConfiguration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Configures input- and output-directories as well as other file related options.
 */
public abstract class Files {

    /**
     * @return The base path of the input directory.
     */
    @InputDirectory
    public abstract DirectoryProperty getInputBaseDirectory();

    /**
     * @return The base path of the output directory.
     */
    @OutputDirectory
    public abstract DirectoryProperty getOutputBaseDirectory();

    /**
     * @return The number of lines to skip in each .sql file.
     */
    @Input
    public abstract Property<Integer> getSkipLines();

    /**
     * @return The file ending to use while searching for SQL files (default: <strong>.sql</strong>).
     */
    @Input
    public abstract Property<String> getSqlFilesSuffix();

    /**
     * @return The charset to use while reading .sql files (default: <strong>UTF-8</strong>).
     */
    @Input
    public abstract Property<Charset> getSqlFilesCharset();

    /**
     * @return The separator to split SQL statements inside a single .sql file (default: <strong>;</strong>).
     */
    @Input
    public abstract Property<String> getSqlStatementSeparator();

    FilesConfiguration asConfiguration() {
        return FilesConfiguration.usingDefaults()
                .setInputBaseDirectory(getInputBaseDirectory().get().getAsFile().toPath())
                .setOutputBaseDirectory(getOutputBaseDirectory().get().getAsFile().toPath())
                .setSkipLines(getSkipLines().get())
                .setSqlFilesSuffix(getSqlFilesSuffix().get())
                .setSqlFilesCharset(getSqlFilesCharset().get())
                .setSqlStatementSeparator(getSqlStatementSeparator().get())
                .build();
    }

    void configureConventions(final ProjectLayout layout) {
        getInputBaseDirectory().convention(layout.getProjectDirectory().dir("src/main/yosql"));
        getOutputBaseDirectory().convention(layout.getBuildDirectory().dir("generated/sources/yosql"));
        getSkipLines().convention(0);
        getSqlFilesSuffix().convention(".sql");
        getSqlFilesCharset().convention(StandardCharsets.UTF_8);
        getSqlStatementSeparator().convention(";");
    }

}
