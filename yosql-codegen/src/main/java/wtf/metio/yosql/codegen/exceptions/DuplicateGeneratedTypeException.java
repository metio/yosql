/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.util.List;

/**
 * Signals that two of the types about to be written would be written as the same one.
 *
 * <p>Every generated type is one file, named after itself. Two of them agreeing on a package and a
 * name is one file written twice, and whichever runs second is the one that survives — so a
 * generator that asked for a record gets an interface instead, and the failure lands in
 * {@code javac} on a converter nobody wrote, saying the type it builds is abstract.</p>
 *
 * <p>The names that can collide are settled by different rules that never see each other: a
 * repository interface is the repository's name without its suffix, a generated record is whatever
 * {@code resultRowType} says, and a converter is a prefix and a suffix around a record's name.
 * Nothing about any one of them is wrong on its own.</p>
 */
public final class DuplicateGeneratedTypeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DuplicateGeneratedTypeException(final String type, final List<String> kinds) {
        super(("Two generated types would both be written as '%s': %s. One file cannot be both, and "
                + "whichever is written second is the one that would survive. The names come from "
                + "different settings — a repository interface is the repository's name without its "
                + "'Repository' suffix, so statements in a 'document' directory give an interface "
                + "named 'Document' — so rename whichever of them is yours to name, or turn off "
                + "'repositories.generateInterfaces'.")
                .formatted(type, String.join(" and ", kinds)));
    }

}
