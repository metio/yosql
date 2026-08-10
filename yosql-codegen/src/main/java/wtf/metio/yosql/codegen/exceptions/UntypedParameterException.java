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

    /**
     * @param tablesRead     the tables the schema came out holding, for the case this most often is:
     *                       the schema was read, and the table this statement needs is not in it
     * @param tablesInScope  the tables this statement reads from, or {@code null} when it reads from
     *                       something that cannot be enumerated — a subquery, a common table
     *                       expression, a function call
     * @param columnsInScope the columns those tables hold, which are the names a parameter can be
     *                       inferred from
     */
    public UntypedParameterException(
            final Path source,
            final String statement,
            final Collection<String> parameters,
            final Collection<String> tablesRead,
            final Collection<String> tablesInScope,
            final Collection<String> columnsInScope) {
        super(message(source, statement, parameters, tablesRead, tablesInScope, columnsInScope));
    }

    private static String message(
            final Path source,
            final String statement,
            final Collection<String> parameters,
            final Collection<String> tablesRead,
            final Collection<String> tablesInScope,
            final Collection<String> columnsInScope) {
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
        // The input is the parameter's *name*, and nothing said so. A message that lists what the
        // schema holds and never mentions the name reads as "the schema is short", which sends a
        // reader to their migrations and their vendor setting — both of them fine — while the
        // statement in front of them compares ':tenantId' against a column called 'id'.
        message.append("\n\n  A parameter takes its type from a column of the same name in the ");
        message.append("tables this statement reads, 'camelCase' read as 'snake_case'. ");
        if (tablesInScope == null) {
            message.append("This statement reads from something that cannot be listed — a subquery, ");
            message.append("a common table expression or a function call — so no column of it is ");
            message.append("matched against, and every parameter here needs its type named.");
        } else if (columnsInScope.isEmpty()) {
            message.append("It reads from ").append(String.join(", ", tablesInScope));
            message.append(", and the schema describes no columns of those with a type, so there is ");
            message.append("nothing to match against.");
        } else {
            message.append("It reads ").append(String.join(", ", tablesInScope));
            message.append(", holding: ").append(String.join(", ", columnsInScope)).append(".");
            message.append("\n  Rename the parameter after the column it means, or name its type ");
            message.append("above — a name that says more than the column does is a good reason to ");
            message.append("keep it and declare the type.");
        }
        // What the schema came out holding, because the answer is so often "the table this needs is
        // not in it". A schema is checked silently — a statement reading a table nobody described is
        // skipped rather than failed — so this is the first place a reader learns the catalog is
        // short, and reading it as "name the type" sends them to fix the wrong thing.
        //
        // Only where it can still explain something. Once the columns above are listed, the reader
        // has seen what was matched against and what the parameter is called; repeating what the
        // schema holds after that points back at a schema that is not the problem, which is the way
        // this message has misled before.
        if (columnsInScope.isEmpty()) {
            message.append("\n\n  The schema read ").append(tablesRead.size()).append(" table(s)");
            if (tablesRead.isEmpty()) {
                message.append(", so nothing could be inferred from it. Check that 'schema."
                        + "sqlStatementsDirectory' points at your DDL, or keep your 'create table' "
                        + "statements among the SQL files YoSQL already reads.");
            } else {
                message.append(": ").append(String.join(", ", tablesRead))
                        .append(".\n  A parameter named after a column of a table that is not in that "
                                + "list has nothing to be inferred from.");
            }
        }
        return message.toString();
    }

}
