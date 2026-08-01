/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that a statement's result row type is neither a record nor a value a column can hold.
 */
public final class UnreadableResultRowTypeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnreadableResultRowTypeException(final String statement, final String type, final String reason) {
        super("Statement '%s' declares 'resultRowType: %s', which %s".formatted(statement, type, reason));
    }

}
