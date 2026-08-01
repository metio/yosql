/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import com.palantir.javapoet.ClassName;

import java.io.Serial;
import java.nio.file.Path;

/**
 * Signals that no source file was found for a type named as a result row type.
 */
public final class MissingRecordSourceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MissingRecordSourceException(final ClassName type, final Path expected, final String statement) {
        super(("Statement '%s' declares 'resultRowType: %s', but no source file for it exists at %s. "
                + "Point 'sourceDirectory' at the directory the record is compiled from, or correct the type name.")
                .formatted(statement, type, expected));
    }

}
