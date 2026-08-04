/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that the configured statement separator is not something a file can be split on.
 *
 * <p>The separator is matched literally, so any text is a usable one — except no text at all, which
 * would either never be found or be found between every pair of characters.</p>
 */
public final class UnusableStatementSeparatorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnusableStatementSeparatorException(final String setting, final String separator) {
        super(("'%s' is set to [%s], which is empty. The separator is the text that stands between two "
                + "statements, so it has to be something a file can be split on — ';' by default.")
                .formatted(setting, separator));
    }

}
