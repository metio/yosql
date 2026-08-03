/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that a collection parameter belongs to a statement written once per database vendor.
 *
 * <p>Each vendor writes its own SQL, so each may place its parameters differently; one method binds
 * all of them, and it can only count placeholders in one order.</p>
 */
public final class UnexpandableParameterException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnexpandableParameterException(final String statement, final String parameter) {
        super(("Statement '%s' has a collection parameter '%s' and is also written once per vendor. "
                + "Each vendor's SQL may place its parameters differently, and one method cannot bind "
                + "them in two orders. Give the statement one form for every database, or pass '%s' as "
                + "a single value.")
                .formatted(statement, parameter, parameter));
    }

}
