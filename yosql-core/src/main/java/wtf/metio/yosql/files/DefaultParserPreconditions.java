/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.files;

import wtf.metio.yosql.i18n.Translator;
import wtf.metio.yosql.model.errors.FileErrors;
import wtf.metio.yosql.model.errors.ExecutionErrors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class DefaultParserPreconditions implements ParserPreconditions {

    private final ExecutionErrors errors;
    private final Translator translator;

    DefaultParserPreconditions(
            final ExecutionErrors errors,
            final Translator translator) {
        this.errors = errors;
        this.translator = translator;
    }

    @Override
    public void assertDirectoryIsWriteable(final Path directory) {
        if (!directory.toFile().exists()) {
            try {
                if (Files.createDirectories(directory) != null) {
                    errors.illegalState(translator.nonLocalized(FileErrors.CANNOT_CREATE_DIRECTORY, directory));
                }
            } catch (final IOException cause) {
                errors.illegalState(cause, translator.nonLocalized(FileErrors.DIRECTORY_CREATION_FAILED, directory));
            }
        }
        if (!directory.toFile().isDirectory()) {
            errors.illegalState(translator.nonLocalized(FileErrors.NOT_A_DIRECTORY, directory));
        }
        if (!Files.isWritable(directory)) {
            errors.illegalState(translator.nonLocalized(FileErrors.NO_WRITE_PERMISSION, directory));
        }
    }

    @Override
    public void assertDirectoryIsReadable(final Path directory) {
        if (!directory.toFile().exists()) {
            errors.illegalState(translator.nonLocalized(FileErrors.NOT_EXISTS, directory));
        }
        if (!directory.toFile().isDirectory()) {
            errors.illegalState(translator.nonLocalized(FileErrors.NOT_A_DIRECTORY, directory));
        }
        if (!Files.isReadable(directory)) {
            errors.illegalState(translator.nonLocalized(FileErrors.NO_READ_PERMISSION, directory));
        }
    }

}
