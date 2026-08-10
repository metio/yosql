/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.nio.file.Path;

/**
 * Signals that a type about to be generated is a type the project already has.
 *
 * <p>Separate from {@link DuplicateGeneratedTypeException} because the advice differs and so does
 * the fix: nothing here can be resolved by renaming something else that was generated, and the file
 * that already exists is one the reader wrote and may not want moved.</p>
 */
public final class GeneratedTypeExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public GeneratedTypeExistsException(final String type, final String kind, final Path existing) {
        super(("YoSQL would generate %s '%s', and %s already declares that type. Two files claiming "
                + "one name is a duplicate class, reported by the compiler in whichever of them it "
                + "reaches first — often the generated one, which names neither this file nor the "
                + "setting that chose the name. A repository interface is the repository's name "
                + "without its 'Repository' suffix, so statements in a 'windDown' directory generate "
                + "an interface called 'WindDown'. Rename one of the two, move yours to another "
                + "package, or turn off 'repositories.generateInterfaces'.")
                .formatted(kind, type, existing));
    }

}
