package de.xn__ho_hia.yosql.parser;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import javax.inject.Inject;

import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;

/**
 * A SQL file resolver that starts at the given source, walks into every subdirectory and returns all .sql files.
 */
public final class PathBasedSqlFileResolver implements SqlFileResolver {

    private final ParserPreconditions    preconditions;
    private final ExecutionErrors        errors;
    private final ExecutionConfiguration configuration;

    /**
     * @param preconditions
     *            The preconditions to use.
     * @param errors
     *            The error collector to use.
     * @param configuration
     *            The configuration to use.
     */
    @Inject
    public PathBasedSqlFileResolver(
            final ParserPreconditions preconditions,
            final ExecutionErrors errors,
            final ExecutionConfiguration configuration) {
        this.preconditions = preconditions;
        this.errors = errors;
        this.configuration = configuration;
    }

    @Override
    public Stream<Path> resolveFiles() {
        final Path source = configuration.inputBaseDirectory();
        preconditions.assertDirectoryIsReadable(source);

        if (!errors.hasErrors()) {
            try {
                return Files.walk(source, FileVisitOption.FOLLOW_LINKS)
                        .parallel()
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".sql")); //$NON-NLS-1$
            } catch (final IOException | SecurityException exception) {
                errors.add(exception);
            }
        }

        return Stream.empty();
    }

}
