/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import java.nio.file.Path;

/**
 * Preconditions that have to be matched for SQL parsing to work.
 */
public interface ParserPreconditions { // TODO: rename to CodegenPreconditions b/c writing is for generators not parsers

    /**
     * Asserts that a single directory is writable. In order to be writable, the directory has to:
     * <ul>
     * <li>exist</li>
     * <li>be a directory (not a file)</li>
     * <li>be writable by the current process</li>
     * </ul>
     *
     * @param directory The directory to check
     */
    void assertDirectoryIsWriteable(Path directory);

    /**
     * Asserts that a single directory is readable. In order to be readable, the directory has to:
     * <ul>
     * <li>exist</li>
     * <li>be a directory (not a file)</li>
     * <li>be readable by the current process</li>
     * </ul>
     *
     * @param directory The directory to check
     */
    void directoryIsReadable(Path directory);

}
