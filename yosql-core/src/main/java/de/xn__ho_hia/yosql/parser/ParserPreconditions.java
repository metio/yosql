/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.inject.Inject;

import de.xn__ho_hia.yosql.model.ExecutionErrors;

/**
 * Preconditios that have to be matched for SQL parsing to work.
 */
public class ParserPreconditions {

    private final ExecutionErrors errors;

    /**
     * @param errors
     *            The error collector to use.
     */
    @Inject
    public ParserPreconditions(final ExecutionErrors errors) {
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
        if (!directory.toFile().exists()) {
            try {
                if (Files.createDirectories(directory) != null) {
                    errors.illegalState("Could not create [%s]. Check the permissions.", directory);
                }
            } catch (final IOException cause) {
                errors.illegalState("Failure during directory creation: %s", cause.getMessage());
            }
        }
        if (!directory.toFile().isDirectory()) {
            errors.illegalState("[%s] is not a directory.", directory);
        }
        if (!Files.isWritable(directory)) {
            errors.illegalState("Don't have permission to write to [%s].", directory);
        }
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
    public void assertDirectoryIsReadable(final Path directory) {
        if (!directory.toFile().exists()) {
            errors.illegalState("[%s] does not exist.", directory);
        }
        if (!directory.toFile().isDirectory()) {
            errors.illegalState("[%s] is not a directory.", directory);
        }
        if (!Files.isReadable(directory)) {
            errors.illegalState("[%s] no permission to read.", directory);
        }
    }

}
