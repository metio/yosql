/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Signals that the columns a statement selects and the components of its result row type do not
 * line up. Both directions are defects the generator can see before anything is compiled: a
 * component nothing supplies would read a column that is not there, and a selected column nothing
 * claims is either a typo or work the database did for nobody.
 */
public final class UnmappedColumnsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnmappedColumnsException(
            final Path source,
            final String statement,
            final String resultRowType,
            final Collection<String> componentsWithoutColumn,
            final Collection<String> columnsWithoutComponent) {
        super(message(source, statement, resultRowType, componentsWithoutColumn, columnsWithoutComponent));
    }

    private static String message(
            final Path source,
            final String statement,
            final String resultRowType,
            final Collection<String> componentsWithoutColumn,
            final Collection<String> columnsWithoutComponent) {
        final var message = new StringBuilder("Statement '%s' in %s cannot be mapped to %s."
                .formatted(statement, source, resultRowType));
        if (!componentsWithoutColumn.isEmpty()) {
            message.append("\n  No selected column supplies: ")
                    .append(String.join(", ", componentsWithoutColumn))
                    .append(".\n  Select the column, or alias an existing one to that name.");
        }
        if (!columnsWithoutComponent.isEmpty()) {
            message.append("\n  No component claims: ")
                    .append(String.join(", ", columnsWithoutComponent))
                    .append(".\n  Drop the column from the select, or add a component for it.");
        }
        return message.toString();
    }

}
