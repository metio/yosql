/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import com.palantir.javapoet.ClassName;

import java.io.Serial;

/**
 * Signals that a result row type contains itself.
 */
public final class RecursiveRecordException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public RecursiveRecordException(final ClassName type, final String path) {
        super(("%s contains itself at '%s'. A row is flat, so there is no depth at which the "
                + "nesting would stop.")
                .formatted(type, path));
    }

}
