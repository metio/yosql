/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.util.Collection;

/**
 * Signals that one repository needs two converters that would occupy the same field.
 *
 * <p>A converter's field is named after its class, so two converter classes sharing a simple name
 * collide however different their packages are. Generating both would emit a class that does not
 * compile, which is a worse way to learn about it than this.</p>
 */
public final class DuplicateConverterAliasException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateConverterAliasException(final String alias, final Collection<String> converterTypes) {
        super(("Statements in one repository name %s, which would both be held in a field called '%s'. "
                + "A converter's field is named after its class, so rename one of them, or move the "
                + "statements into repositories of their own.")
                .formatted(String.join(" and ", converterTypes), alias));
    }

}
