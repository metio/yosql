/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.nio.file.Path;

/**
 * Signals that a statement asked for its result row type to be written, but two of the columns it
 * selects would give the record the same component twice.
 *
 * <p>Which is what a star over a join means whenever both tables declare a column of the same name -
 * an {@code id} apiece, most of the time. The row really does carry both, so the statement is fine;
 * only the record is impossible, and a reader who is told which name collided can alias one of them
 * in the select list.</p>
 */
public final class CollidingResultColumnsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CollidingResultColumnsException(
            final Path source,
            final String statement,
            final String component) {
        super(("Statement '%s' in %s selects two columns that would both be named '%s' in the result "
                + "row type YoSQL was asked to write, and a record cannot hold the same component "
                + "twice. Write the select list out with an alias for one of them - "
                + "'select a.id as accountId, t.id as tenantId' rather than 'select *'.")
                .formatted(statement, source, component));
    }

}
