/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import com.palantir.javapoet.ClassName;

import java.io.Serial;

/**
 * Signals that a statement asked for its result row type to be written, but the schema does not say
 * enough about what it selects to write one.
 *
 * <p>Separate from {@link MissingRecordSourceException} because the advice differs: a statement that
 * writes its own record is not missing a source file, and telling its author to point
 * {@code sourceDirectory} somewhere else would send them looking for a file that is not supposed to
 * exist.</p>
 */
public final class UnknownRecordShapeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnknownRecordShapeException(final ClassName type, final String statement, final String reason) {
        // The reason carries its own advice: four unrelated things end in a shape nobody can write,
        // and a message listing every remedy leaves the reader to work out which one they are.
        super("Statement '%s' asks YoSQL to write '%s', but the schema does not say what every column "
                .formatted(statement, type) + "it selects holds: " + reason);
    }

}
