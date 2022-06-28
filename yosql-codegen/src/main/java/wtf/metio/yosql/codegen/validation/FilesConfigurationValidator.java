/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
        directoryIsWriteable(files.outputBaseDirectory());
    }

    private void directoryIsWriteable(final Path directory) {
        if (Files.notExists(directory)) {
            try {
                if (Files.createDirectories(directory) == null) {
                    errors.illegalState(messages.getMessage(FileErrors.CANNOT_CREATE_DIRECTORY, directory));
                }
            } catch (final IOException cause) {
                errors.illegalState(cause, messages.getMessage(FileErrors.DIRECTORY_CREATION_FAILED, directory));
            }
        }
        if (!Files.isDirectory(directory)) {
            errors.illegalState(messages.getMessage(FileErrors.NOT_A_DIRECTORY, directory));
        }
        if (!Files.isWritable(directory)) {
            errors.illegalState(messages.getMessage(FileErrors.NO_WRITE_PERMISSION, directory));
        }
    }

    private void directoryIsReadable(final Path directory) {
        if (Files.notExists(directory)) {
            errors.illegalState(messages.getMessage(FileErrors.NOT_EXISTS, directory));
        }
        if (!Files.isDirectory(directory)) {
            errors.illegalState(messages.getMessage(FileErrors.NOT_A_DIRECTORY, directory));
        }
        if (!Files.isReadable(directory)) {
            errors.illegalState(messages.getMessage(FileErrors.NO_READ_PERMISSION, directory));
        }
    }

}
