/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import com.squareup.javapoet.ClassName;

import java.io.Serial;
import java.nio.file.Path;

/**
 * Signals that no source file was found for a type named as a result row converter.
 */
public final class MissingConverterSourceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public MissingConverterSourceException(final ClassName type, final Path expected, final String origin) {
        super(("%s names the converter '%s', but no source file for it exists at %s. "
                + "A converter is read from source to find the method to call, so point 'sourceDirectory' at the "
                + "directory the converter is compiled from, or correct the class name.")
                .formatted(origin, type, expected));
    }

}
