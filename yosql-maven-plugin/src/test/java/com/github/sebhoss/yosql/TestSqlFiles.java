package com.github.sebhoss.yosql;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class TestSqlFiles {

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
