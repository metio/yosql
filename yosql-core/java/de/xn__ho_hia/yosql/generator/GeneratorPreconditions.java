package de.xn__ho_hia.yosql.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Inject;

import de.xn__ho_hia.yosql.model.ExecutionErrors;

/**
 * Preconditios that have to be matched before code can be generated.
 */
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
     */
    @SuppressWarnings("nls")
    public void assertDirectoryIsWriteable(final Path directory) {
        if (!Files.exists(directory)) {
            try {
                if (Files.createDirectories(directory) != null) {
                    errors.illegalState("Could not create [%s]. Check the permissions.", directory);
                }
            } catch (final IOException cause) {
                errors.illegalState("Failure during directory creation: %s", cause.getMessage());
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
