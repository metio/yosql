/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.exceptions;

import com.squareup.javapoet.ClassName;

import java.io.Serial;
import java.nio.file.Path;

/**
 * Signals that a type named as a result row type could not be read from its source file.
 */
public final class UnparsableRecordException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnparsableRecordException(final Path location, final ClassName type, final String reason) {
        super("Cannot read the record %s from %s: %s".formatted(type, location, reason));
    }

}
