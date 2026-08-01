/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import com.palantir.javapoet.ClassName;

import java.io.Serial;

/**
 * Signals that a value type used as a parameter is not a record, so the generator cannot tell which
 * accessor returns the value it was built from.
 */
public final class NonRecordValueTypeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NonRecordValueTypeException(final String parameter, final ClassName type) {
        super(("Parameter '%s' has type %s, which declares valueOf but is not a record. Reading one "
                + "needs only that factory, but writing one needs the accessor it came from, and a "
                + "class does not say which of its methods that is. Make %s a record, or declare the "
                + "parameter as the type %s wraps.")
                .formatted(parameter, type, type.simpleName(), type.simpleName()));
    }

}
