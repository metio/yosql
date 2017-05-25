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

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.Translator;

/**
 * Preconditions that have to be matched for SQL parsing to work.
 */
public final class ParserPreconditions {

    private final ExecutionErrors errors;
    private final Translator      translator;

    /**
     * @param errors
     *            The error collector to use.
     * @param translator
     *            The translator to use.
     */
    @Inject
    public ParserPreconditions(
            final ExecutionErrors errors,
            final Translator translator) {
        this.errors = errors;
        this.translator = translator;
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
    public void assertDirectoryIsWriteable(final Path directory) {
        if (!directory.toFile().exists()) {
            try {
                if (Files.createDirectories(directory) != null) {
                    errors.illegalState(translator.nonLocalized(I18N.CANNOT_CREATE_DIRECTORY, directory));
                }
            } catch (final IOException cause) {
                errors.illegalState(translator.nonLocalized(I18N.DIRECTORY_CREATION_FAILED, directory));
            }
        }
        if (!directory.toFile().isDirectory()) {
            errors.illegalState(translator.nonLocalized(I18N.NOT_A_DIRECTORY, directory));
        }
        if (!Files.isWritable(directory)) {
            errors.illegalState(translator.nonLocalized(I18N.NO_WRITE_PERMISSION, directory));
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
    public void assertDirectoryIsReadable(final Path directory) {
        if (!directory.toFile().exists()) {
            errors.illegalState(translator.nonLocalized(I18N.NOT_EXISTS, directory));
        }
        if (!directory.toFile().isDirectory()) {
            errors.illegalState(translator.nonLocalized(I18N.NOT_A_DIRECTORY, directory));
        }
        if (!Files.isReadable(directory)) {
            errors.illegalState(translator.nonLocalized(I18N.NO_READ_PERMISSION, directory));
        }
    }

    @LocaleData(@Locale("en"))
    @BaseName("parser-preconditions")
    static enum I18N {

        NO_READ_PERMISSION, NO_WRITE_PERMISSION, NOT_A_DIRECTORY, NOT_EXISTS, DIRECTORY_CREATION_FAILED, CANNOT_CREATE_DIRECTORY;

    }

}
