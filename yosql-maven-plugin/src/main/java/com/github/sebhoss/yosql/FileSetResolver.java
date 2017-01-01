package com.github.sebhoss.yosql;

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
