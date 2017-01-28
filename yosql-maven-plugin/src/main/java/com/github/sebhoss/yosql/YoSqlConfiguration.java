package com.github.sebhoss.yosql;

import java.io.File;

import org.apache.maven.model.FileSet;

/**
 * Contains plugin-wide configuration & default values.
 */
public final class YoSqlConfiguration {

    /**
     * @param baseDirectory
     * @return A {@link FileSet} for the relative path
     *         <code>src/main/yosql</code> which includes all
     *         <strong>.sql</strong> files.
     */
    public static FileSet defaultSqlFileSet(final File baseDirectory) {
        final FileSet fileSet = new FileSet();
        fileSet.setDirectory(baseDirectory.getAbsolutePath() + "/src/main/yosql");
        fileSet.addInclude("**/*.sql");
        return fileSet;
    }

}
