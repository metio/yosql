/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.nio.file.Path;

/**
 * Signals that a statement names a parameter after something the generated method already declares.
 *
 * <p>Both become identifiers in the same method, so one of them has to give. Saying so here beats
 * emitting a class whose only symptom is a Java error about a variable already defined, in a file
 * the author did not write.</p>
 */
public final class ReservedParameterNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ReservedParameterNameException(final Path source, final String statement, final String parameter) {
        super(("Statement '%s' in %s names a parameter '%s', which is also what the generated method "
                + "calls one of its own variables. Rename the parameter in the SQL — the name reaches "
                + "no further than the method's signature.")
                .formatted(statement, source, parameter));
    }

}
