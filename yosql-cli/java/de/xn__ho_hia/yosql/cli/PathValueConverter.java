package de.xn__ho_hia.yosql.cli;

import java.nio.file.Path;
import java.nio.file.Paths;

import joptsimple.ValueConverter;

/**
 * Convert {@link String} based inputs into {@link Path}s.
 */
public class PathValueConverter implements ValueConverter<Path> {

    @Override
    public Path convert(final String arg0) {
        return Paths.get(arg0);
    }

    @Override
    public String valuePattern() {
        return "/path/to/your/files"; //$NON-NLS-1$
    }

    @Override
    public Class<? extends Path> valueType() {
        return Path.class;
    }

}
