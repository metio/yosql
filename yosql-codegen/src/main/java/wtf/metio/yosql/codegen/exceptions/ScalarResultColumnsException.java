/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Signals that a statement selects more than one column while its result row type holds one value.
 */
public final class ScalarResultColumnsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ScalarResultColumnsException(
            final Path source,
            final String statement,
            final String resultRowType,
            final Collection<String> columns) {
        super(("Statement '%s' in %s selects %d columns (%s), but its result row type %s holds one "
                + "value and would read only the first. Select one column, or use a record with a "
                + "component per column.")
                .formatted(statement, source, columns.size(), String.join(", ", columns), resultRowType));
    }

}
