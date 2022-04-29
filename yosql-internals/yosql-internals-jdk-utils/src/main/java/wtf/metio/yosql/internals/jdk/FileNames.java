/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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

    private FileNames() {
        // utility class
    }

}
