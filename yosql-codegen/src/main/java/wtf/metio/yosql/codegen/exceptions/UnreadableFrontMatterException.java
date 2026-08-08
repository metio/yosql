/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.exceptions;

import java.nio.file.Path;

/**
 * Thrown when a statement's YAML front matter cannot be parsed.
 *
 * <p>The parser's own message describes the YAML and nothing else — {@code while scanning a simple
 * key} names a line and a column of a fragment the author never sees as a file. In a project with
 * hundreds of statements that leaves nothing to open, so the file and the parser's account of the
 * problem are reported together.</p>
 *
 * <p>The commonest cause is a value carried over a second line. Front matter lives in {@code --}
 * comments, one key to a line, so a {@code description} that wraps puts a bare sentence where YAML
 * expects the next key.</p>
 */
public final class UnreadableFrontMatterException extends RuntimeException {

    public UnreadableFrontMatterException(final Path source, final Throwable cause) {
        super(("Cannot read the front matter of '%s': %s. Front matter is YAML written one key to a "
                + "'--' line; a value continued on a line of its own is read as another key.")
                .formatted(source, cause.getMessage()), cause);
    }

}
