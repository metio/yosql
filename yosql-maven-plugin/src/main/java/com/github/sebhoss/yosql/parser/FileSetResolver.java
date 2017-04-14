/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.github.sebhoss.yosql.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import javax.inject.Inject;

import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;

@SuppressWarnings({ "javadoc" })
public class FileSetResolver implements SqlFileResolver {

    private final ExecutionErrors pluginErrors;
    private final FileSet         fileSet;

    @Inject
    public FileSetResolver(final ExecutionErrors pluginErrors, final FileSet fileSet) {
        this.pluginErrors = pluginErrors;
        this.fileSet = fileSet;
    }

    @Override
    public Stream<Path> resolveFiles() {
        final File directory = new File(fileSet.getDirectory());
        final String includes = commaSeparated(fileSet.getIncludes());
        final String excludes = commaSeparated(fileSet.getExcludes());
        try {
            return FileUtils.getFiles(directory, includes, excludes)
                    .stream()
                    .map(File::toPath);
        } catch (final IOException exception) {
            pluginErrors.add(exception);
            return Stream.of();
        }
    }

}
