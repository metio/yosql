/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.validation;

import ch.qos.cal10n.IMessageConveyor;
import wtf.metio.yosql.codegen.lifecycle.FileErrors;
import wtf.metio.yosql.codegen.orchestration.ExecutionErrors;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Validates {@link wtf.metio.yosql.models.immutables.FilesConfiguration}s.
 */
public final class FilesConfigurationValidator implements RuntimeConfigurationValidator {

    private final ExecutionErrors errors;
    private final IMessageConveyor messages;

    public FilesConfigurationValidator(final ExecutionErrors errors, final IMessageConveyor messages) {
        this.errors = errors;
        this.messages = messages;
    }

    @Override
    public void validate(final RuntimeConfiguration configuration) {
        final var files = configuration.files();

        directoryIsReadable(files.inputBaseDirectory());
        directoryIsWritable(files.outputBaseDirectory());
    }

    /**
     * Each check stops at the first thing that is wrong.
     *
     * <p>A directory that does not exist is also not a directory and also not readable, and saying
     * all three is three problems in the build log where the developer has one. The first answer is
     * the actionable one; the rest follow from it.</p>
     */
    private void directoryIsWritable(final Path directory) {
        if (Files.notExists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (final IOException cause) {
                errors.illegalState(cause, messages.getMessage(FileErrors.DIRECTORY_CREATION_FAILED, directory));
                return;
            }
        }
        if (!Files.isDirectory(directory)) {
            errors.illegalState(messages.getMessage(FileErrors.NOT_A_DIRECTORY, directory));
        } else if (!Files.isWritable(directory)) {
            errors.illegalState(messages.getMessage(FileErrors.NO_WRITE_PERMISSION, directory));
        }
    }

    private void directoryIsReadable(final Path directory) {
        if (Files.notExists(directory)) {
            errors.illegalState(messages.getMessage(FileErrors.NOT_EXISTS, directory));
        } else if (!Files.isDirectory(directory)) {
            errors.illegalState(messages.getMessage(FileErrors.NOT_A_DIRECTORY, directory));
        } else if (!Files.isReadable(directory)) {
            errors.illegalState(messages.getMessage(FileErrors.NO_READ_PERMISSION, directory));
        }
    }

}
