/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 */
public final class TestSqlFiles {

    /**
     * @param relativeLocation The location of the SQL file to load.
     * @return The full path to the SQL file.
     */
    public static Path getFullPath(final String relativeLocation) {
        try {
            final URL resource = TestSqlFiles.class.getResource(relativeLocation);
            final URI uri = resource.toURI();
            return Paths.get(uri);
        } catch (final URISyntaxException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

}
