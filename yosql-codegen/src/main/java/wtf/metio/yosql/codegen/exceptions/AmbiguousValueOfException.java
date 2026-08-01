/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import com.palantir.javapoet.TypeName;

import java.io.Serial;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Signals that a type offers more than one way to be built from a column.
 */
public final class AmbiguousValueOfException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AmbiguousValueOfException(
            final String component, final TypeName type, final Collection<TypeName> parameters) {
        super(("Component '%s' has type %s, which declares valueOf for %s. "
                + "A column can be read as one of them, and picking silently would be a guess — "
                + "leave a single valueOf taking the type the column holds.")
                .formatted(component, type,
                        parameters.stream().map(TypeName::toString).collect(Collectors.joining(" and "))));
    }

}
