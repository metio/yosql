/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.nio.file.Path;

/**
 * Signals that a column a statement selects cannot name a record component.
 *
 * <p>A generated result row type takes a component per selected column, named after it — and a
 * column is under no obligation to be a Java identifier. {@code select lat, long from places} is
 * ordinary SQL and {@code long} is a keyword, so the record cannot be written. Saying which column
 * beats JavaPoet's {@code not a valid name}, which names neither the statement nor the column.</p>
 */
public final class UnusableComponentNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnusableComponentNameException(
            final Path source,
            final String statement,
            final String column) {
        super(("Statement '%s' in %s selects a column named '%s', which cannot name a component of "
                + "the result row type YoSQL was asked to write. Give it an alias the record can use "
                + "— 'select %s as %sValue' — or write the record by hand.")
                .formatted(statement, source, column, column, column));
    }

}
