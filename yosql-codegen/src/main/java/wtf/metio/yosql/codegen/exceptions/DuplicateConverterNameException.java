/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.exceptions;

import com.squareup.javapoet.ClassName;

import java.io.Serial;

/**
 * Signals that two result row types would be served by converters of the same name.
 */
public final class DuplicateConverterNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateConverterNameException(
            final ClassName converter, final ClassName first, final ClassName second) {
        super(("%s would be generated for both %s and %s, and only one of them can have it. "
                + "Rename one of the records, or give one of the statements an explicit "
                + "'resultRowConverter' instead.")
                .formatted(converter, first, second));
    }

}
