/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import com.palantir.javapoet.TypeName;

import java.io.Serial;

/**
 * Signals that a record component has a type no column can be read into.
 */
public final class UnsupportedComponentTypeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnsupportedComponentTypeException(final String component, final TypeName type, final String supported) {
        super(("Component '%s' has type %s, which cannot be read from a result set. "
                + "Supported types are %s, any enum, and any record built from those. "
                + "To read this one from a single column, give it a "
                + "'public static %s valueOf(<supported type>)' factory — that is all the generator "
                + "needs in order to build it.")
                .formatted(component, type, supported, simpleNameOf(type)));
    }

    private static String simpleNameOf(final TypeName type) {
        final var name = type.toString();
        final var dot = name.lastIndexOf('.');
        return dot < 0 ? name : name.substring(dot + 1);
    }

}
