/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.exceptions;

import java.io.Serial;
import java.nio.file.Path;

/**
 * Signals that the directory a statement sits in cannot be turned into a Java name.
 *
 * <p>A repository nobody named is named after where its statements live, and a directory is under no
 * obligation to be a Java identifier: {@code user-accounts} is an ordinary directory and
 * {@code User-accountsRepository} is not a class anyone can declare. Saying so while the path is
 * still in view beats the invalid name reaching JavaPoet, which answers with an
 * {@code IllegalArgumentException} naming neither the file nor the directory behind it.</p>
 */
public final class UnusableRepositoryNameException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnusableRepositoryNameException(final Path source, final String segment) {
        super(("The statements in %s would be generated into a repository named after their directory, "
                + "but '%s' is not usable as a Java name. Rename the directory, or name the repository "
                + "yourself with a 'repository:' entry in the statement's front matter or the "
                + "'repositories.basePackageName' setting.")
                .formatted(source, segment));
    }

}
