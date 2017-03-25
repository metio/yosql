package de.xn__ho_hia.yosql.generator;

import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Inject;

import de.xn__ho_hia.yosql.model.ExecutionErrors;

public class GeneratorPreconditions {

    private final ExecutionErrors errors;

    /**
     * @param errors
     *            The error collector to use.
     */
    @Inject
    public GeneratorPreconditions(final ExecutionErrors errors) {
        this.errors = errors;
    }

    /**
     * Asserts that a single directory is writable. In order to be writable, the directory has to:
     * <ul>
     * <li>exist</li>
     * <li>be a directory (not a file)</li>
     * <li>be writable by the current process</li>
     * </ul>
     *
     * @param directory
     *            The directory to check
     * @throws MException
     *             In case the directory is somehow not writeable.
     */
    @SuppressWarnings("nls")
    public void assertDirectoryIsWriteable(final Path directory) throws Exception {
        if (!Files.exists(directory)) {
            if (Files.createDirectories(directory) != null) {
                errors.illegalState("Could not create [%s]. Check the permissions.", directory);
            }
        }
        if (!Files.isDirectory(directory)) {
            errors.illegalState("[%s] is not a directory.", directory);
        }
        if (!Files.isWritable(directory)) {
            errors.illegalState("Don't have permission to write to [%s].", directory);
        }
    }

}
