package wtf.metio.yosql.files;

import java.nio.file.Path;

/**
 * Preconditions that have to be matched for SQL parsing to work.
 */
public interface ParserPreconditions {

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
     * Asserts that a single directory is writable. In order to be writable, the directory has to:
     * <ul>
     * <li>exist</li>
     * <li>be a directory (not a file)</li>
     * <li>be writable by the current process</li>
     * </ul>
     *
     * @param directory The directory to check
     */
    void assertDirectoryIsReadable(Path directory);

}
