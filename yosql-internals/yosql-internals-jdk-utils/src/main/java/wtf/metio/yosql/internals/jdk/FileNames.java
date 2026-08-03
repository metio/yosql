/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.internals.jdk;

import java.nio.file.Path;

/**
 * Utility class that deals with file names.
 */
public final class FileNames {

    /**
     * Extract the file name of a path without its optional file extension.
     *
     * @param path The path to use.
     * @return The file name of the given path without its file extension.
     */
    public static String withoutExtension(final Path path) {
        final var fileName = path.getFileName().toString();
        final var dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }

    /**
     * Converts the given path to its string representation using slashes as separators.
     *
     * @param path The path to convert
     * @return The string representation of the path using slashes
     */
    public static String toSlashes(final Path path) {
        // Joining the path's own segments rather than replacing backslashes in its string: on a
        // Unix filesystem a backslash is an ordinary character in a filename, and replacing it
        // invents a directory boundary that was never there.
        final var joined = new StringBuilder();
        for (final var segment : path) {
            if (!joined.isEmpty()) {
                joined.append('/');
            }
            joined.append(segment);
        }
        return path.isAbsolute() ? "/" + joined : joined.toString();
    }

    private FileNames() {
        // utility class
    }

}
