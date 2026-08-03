/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

/**
 * Signals that the databases a statement runs against declare a column as types that do not meet in
 * Java.
 *
 * <p>A statement naming no vendor is the fallback for every database not named, and it generates one
 * method with one signature. Where one database calls a column a {@code uuid} and another calls it a
 * {@code varchar}, that signature cannot be both, and picking one silently would generate a
 * repository that is wrong against half the databases it claims to support.</p>
 *
 * <p>Most dialect differences never reach here, because they meet in Java: {@code bigserial},
 * {@code bigint auto_increment} and {@code int8} are all a {@code long}. What reaches here is a real
 * disagreement about what the column holds.</p>
 */
public final class ConflictingColumnTypeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ConflictingColumnTypeException(
            final Path source,
            final String statement,
            final Map<String, Set<String>> disagreements) {
        super(message(source, statement, disagreements));
    }

    private static String message(
            final Path source,
            final String statement,
            final Map<String, Set<String>> disagreements) {
        final var message = new StringBuilder(
                "Statement '%s' in %s runs against databases that disagree about what a column holds:"
                        .formatted(statement, source));
        disagreements.forEach((column, types) -> message.append("\n  '").append(column).append("' reads as ")
                .append(String.join(" and ", types)).append('.'));
        message.append("\n\n  The statement names no vendor, so it is the fallback for every database ");
        message.append("not named and generates one method for all of them. Name the type you want in ");
        message.append("its front matter to settle it, or write a statement per vendor.");
        return message.toString();
    }

}
