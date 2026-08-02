/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Signals that a statement binds a parameter whose Java type nothing says.
 *
 * <p>A parameter without a type can only be bound as {@code java.lang.Object}, which compiles and
 * then accepts anything at all: the method a caller reaches for is exactly as type-safe as the JDBC
 * it was meant to replace. Naming the type is the whole point, so a statement that leaves one
 * unnamed and gives the generator no way to work it out fails here instead.</p>
 */
public final class UntypedParameterException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UntypedParameterException(
            final Path source,
            final String statement,
            final Collection<String> parameters) {
        super(message(source, statement, parameters));
    }

    private static String message(
            final Path source,
            final String statement,
            final Collection<String> parameters) {
        final var message = new StringBuilder("Statement '%s' in %s binds %s no type is known for: "
                .formatted(statement, source, parameters.size() == 1 ? "a parameter" : "parameters"));
        message.append(String.join(", ", parameters)).append('.');
        message.append("\n  Name the type in the front matter:\n");
        message.append("\n  -- parameters:");
        parameters.forEach(parameter -> message.append("\n  --   ").append(parameter).append(": <type>"));
        message.append("\n\n  A type is a fully-qualified class name, a primitive, or one of the short ");
        message.append("names such as 'uuid', 'string' or 'instant'.");
        message.append("\n  A statement naming a record with 'resultRowType' takes the type of the ");
        message.append("component of the same name, so a parameter matching one needs nothing here.");
        return message.toString();
    }

}
