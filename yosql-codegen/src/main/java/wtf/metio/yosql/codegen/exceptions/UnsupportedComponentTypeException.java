/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.exceptions;

import com.squareup.javapoet.TypeName;

import java.io.Serial;

/**
 * Signals that a record component has a type no column can be read into.
 */
public final class UnsupportedComponentTypeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnsupportedComponentTypeException(final String component, final TypeName type, final String supported) {
        super(("Component '%s' has type %s, which cannot be read from a result set. "
                + "Supported types are %s, any enum, and any record built from those.")
                .formatted(component, type, supported));
    }

}
