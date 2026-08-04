/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that a statement declares parameters but asks not to be prepared.
 *
 * <p>A plain {@link java.sql.Statement} takes its query as text and has no way to bind a value to a
 * placeholder; only a {@link java.sql.PreparedStatement} does. Pasting the values into the text
 * instead is how SQL injection happens, so the two settings cannot both be honoured.</p>
 */
public final class UnbindableStatementException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnbindableStatementException(final String statement, final String parameter) {
        super(("Statement '%s' declares the parameter '%s' and sets 'usePreparedStatement' to false. "
                + "A plain java.sql.Statement cannot bind a value to a placeholder. Leave "
                + "'usePreparedStatement' at true, or write a statement that takes no parameters.")
                .formatted(statement, parameter));
    }

}
