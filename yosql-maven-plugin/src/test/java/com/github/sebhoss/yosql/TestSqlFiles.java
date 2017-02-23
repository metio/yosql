/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.github.sebhoss.yosql;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 *
 */
public final class TestSqlFiles {

    /**
     * @param relativeLocation
     *            The location of the SQL file to load.
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
