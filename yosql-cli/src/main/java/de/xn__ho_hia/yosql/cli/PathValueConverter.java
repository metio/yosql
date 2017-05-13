/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.xn__ho_hia.yosql.model.ExecutionErrors;
import joptsimple.ValueConverter;

/**
 * Convert {@link String} based inputs into {@link Path}s.
 */
public class PathValueConverter implements ValueConverter<Path> {

    private final ExecutionErrors errors;

    /**
     * @param errors
     *            The error collector to use.
     */
    public PathValueConverter(final ExecutionErrors errors) {
        this.errors = errors;
    }

    @Override
    public Path convert(final String arg0) {
        try {
            return Paths.get(arg0);
        } catch (final InvalidPathException exception) {
            errors.add(exception);
            return null;
        }
    }

    @Override
    public String valuePattern() {
        return "/path/to/your/files"; //$NON-NLS-1$
    }

    @Override
    public Class<Path> valueType() {
        return Path.class;
    }

}
