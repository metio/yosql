/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;

/**
 * Signals that two statements map the same component of one result row type to different columns.
 */
public final class ConflictingColumnOverrideException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ConflictingColumnOverrideException(
            final String resultRowType,
            final String component,
            final String first,
            final String second,
            final String statement) {
        super(("Component '%s' of %s is mapped to both '%s' and '%s'; statement '%s' declares the second. "
                + "One converter is generated per result row type, so it can only read one of them — "
                + "alias the column in the query that needs the other name.")
                .formatted(component, resultRowType, first, second, statement));
    }

}
