/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import com.squareup.javapoet.ClassName;

import java.io.Serial;
import java.util.Collection;

/**
 * Signals that a parameter's declared type cannot be reduced to a single value to bind.
 */
public final class UnbindableParameterException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnbindableParameterException(
            final String parameter, final ClassName type, final Collection<String> components) {
        super(("Parameter '%s' has type %s, a record of (%s), and a statement binds one value per "
                + "parameter. Declare a parameter for each component, or give %s a "
                + "'public static %s valueOf(<supported type>)' factory and a component of that "
                + "type — which is also what lets it be read back.")
                .formatted(parameter, type, String.join(", ", components), type.simpleName(),
                        type.simpleName()));
    }

}
