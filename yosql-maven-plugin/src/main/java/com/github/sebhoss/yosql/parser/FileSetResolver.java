/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.github.sebhoss.yosql.parser;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.model.FileSet;
import org.codehaus.plexus.util.FileUtils;

import com.github.sebhoss.yosql.model.SqlSourceFile;
import com.github.sebhoss.yosql.plugin.PluginErrors;

@Named
@Singleton
public class FileSetResolver {

    private final PluginErrors pluginErrors;

    @Inject
    public FileSetResolver(final PluginErrors pluginErrors) {
        this.pluginErrors = pluginErrors;
    }

    public Stream<SqlSourceFile> resolveFiles(final FileSet fileSet) {
        final File directory = new File(fileSet.getDirectory());
        final String includes = commaSeparated(fileSet.getIncludes());
        final String excludes = commaSeparated(fileSet.getExcludes());
        try {
            return FileUtils.getFiles(directory, includes, excludes)
                    .stream()
                    .map(File::toPath)
                    .map(sqlFile -> new SqlSourceFile(sqlFile, directory.toPath()));
        } catch (final IOException exception) {
            pluginErrors.add(exception);
            return Stream.of();
        }
    }

    private static String commaSeparated(final List<String> patterns) {
        return patterns.stream().collect(joining(", "));
    }

}
