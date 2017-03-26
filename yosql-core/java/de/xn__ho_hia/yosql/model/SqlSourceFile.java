/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.model;

import java.nio.file.Path;

@SuppressWarnings({ "javadoc" })
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