/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Signals that a {@code resultRowColumns} entry names something the result row type does not have.
 *
 * <p>The entry is how a statement says which column a component reads, so one whose key matches no
 * component changes nothing — and a typo in it is invisible: the component keeps reading the column
 * named after itself, which is usually a column the statement does not select, and the complaint
 * that follows is about the component rather than about the line that was meant to fix it.</p>
 */
public final class UnknownColumnOverrideException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnknownColumnOverrideException(
            final Path source,
            final String statement,
            final String resultRowType,
            final Collection<String> unknown,
            final Collection<String> components) {
        super(("Statement '%s' in %s maps %s under 'resultRowColumns', but %s no component of %s. "
                + "Its components are: %s.")
                .formatted(statement, source, String.join(", ", unknown),
                        unknown.size() == 1 ? "that names" : "those name", resultRowType,
                        String.join(", ", components)));
    }

}
