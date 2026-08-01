/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.nio.file.Path;

/**
 * Signals that a type named as a result row converter cannot be called as one.
 *
 * <p>A converter is named by its class alone, so the method to call has to be unambiguous: exactly
 * one public instance method taking a {@code ResultSet}. Anything else leaves nothing to generate
 * against, and is reported here rather than in the code the user would have to compile.</p>
 */
public final class UnusableConverterException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String REQUIREMENT =
            "A converter has to declare exactly one public method taking a single 'java.sql.ResultSet'; "
                    + "its name is what the repository calls and its return type is what the statement produces.";

    public UnusableConverterException(final String origin, final String reason) {
        super("%s names a converter that cannot be used because %s. %s".formatted(origin, reason, REQUIREMENT));
    }

    public UnusableConverterException(final String origin, final Path location, final String reason) {
        super("%s names a converter that cannot be used because %s. %s See %s."
                .formatted(origin, reason, REQUIREMENT, location));
    }

}
