package com.github.sebhoss.yosql;

import java.nio.file.Path;

public class SqlSourceFile {

    private Path pathToSqlFile;
    private Path baseDirectory;

    public SqlSourceFile(final Path pathToSqlFile, final Path baseDirectory) {
        this.pathToSqlFile = pathToSqlFile;
        this.baseDirectory = baseDirectory;
    }

    /**
     * @return the pathToSqlFile
     */
    public Path getPathToSqlFile() {
        return pathToSqlFile;
    }

    /**
     * @param pathToSqlFile
     *            the pathToSqlFile to set
     */
    public void setPathToSqlFile(final Path pathToSqlFile) {
        this.pathToSqlFile = pathToSqlFile;
    }

    /**
     * @return the baseDirectory
     */
    public Path getBaseDirectory() {
        return baseDirectory;
    }

    /**
     * @param baseDirectory
     *            the baseDirectory to set
     */
    public void setBaseDirectory(final Path baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

}
