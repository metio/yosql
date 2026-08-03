/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Signals that a statement disagrees with the schema it runs against.
 *
 * <p>Every one of these is a defect the database would have reported later, on whichever request
 * reached the statement first. Reporting it here costs a build; reporting it there costs an
 * incident.</p>
 */
public final class SchemaMismatchException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public SchemaMismatchException(
            final Path source,
            final String statement,
            final Collection<String> complaints) {
        super(message(source, statement, complaints));
    }

    private static String message(
            final Path source,
            final String statement,
            final Collection<String> complaints) {
        final var message = new StringBuilder("Statement '%s' in %s does not match the schema:"
                .formatted(statement, source));
        complaints.forEach(complaint -> message.append("\n  ").append(complaint).append('.'));
        message.append("\n\n  The schema comes from the 'create table' statements YoSQL read. ");
        message.append("Where it is wrong about this statement, set 'validateSchema: false' in its ");
        message.append("front matter, or turn 'schema.validation' down to WARN.");
        return message.toString();
    }

}
