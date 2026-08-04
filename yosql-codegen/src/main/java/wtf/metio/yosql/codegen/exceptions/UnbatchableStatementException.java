/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that a statement asks for a batch method it cannot have.
 *
 * <p>A batch method runs one statement many times, once per element of the arrays it is passed. It
 * needs at least one parameter to say how many times that is, and it needs the query text to stay
 * the same across every execution — which is what a collection parameter cannot promise, since each
 * element of the batch could hold a different number of values and so need a different number of
 * placeholders.</p>
 */
public final class UnbatchableStatementException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnbatchableStatementException(final String statement, final String reason) {
        super(("Statement '%s' asks for a batch method, but %s. Set 'executeBatch' to false, or give "
                + "the statement parameters a batch can vary.")
                .formatted(statement, reason));
    }

    public static UnbatchableStatementException withoutParameters(final String statement) {
        return new UnbatchableStatementException(statement,
                "it declares no parameters, so there is nothing for a batch to iterate");
    }

    public static UnbatchableStatementException withCollection(final String statement, final String parameter) {
        return new UnbatchableStatementException(statement, ("its parameter '%s' is a collection, whose "
                + "values each need a placeholder of their own — so every execution of the batch would "
                + "need a different query").formatted(parameter));
    }

}
