package de.xn__ho_hia.yosql.parser;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.slf4j.cal10n.LocLogger;

import de.xn__ho_hia.yosql.model.ApplicationEvents;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;

/**
 * A SQL file resolver that starts at the given source, walks into every subdirectory and returns all .sql files.
 */
final class PathBasedSqlFileResolver implements SqlFileResolver {

    private final ParserPreconditions    preconditions;
    private final ExecutionErrors        errors;
    private final ExecutionConfiguration configuration;
    private final LocLogger              logger;

    PathBasedSqlFileResolver(
            final ParserPreconditions preconditions,
            final ExecutionErrors errors,
            final ExecutionConfiguration configuration,
            final LocLogger logger) {
        this.preconditions = preconditions;
        this.errors = errors;
        this.configuration = configuration;
        this.logger = logger;
    }

    @Override
    public Stream<Path> resolveFiles() {
        final Path source = configuration.inputBaseDirectory();
        preconditions.assertDirectoryIsReadable(source);
        logger.debug(ApplicationEvents.READ_FILES, source);

        if (!errors.hasErrors()) {
            try {
                return Files.walk(source, FileVisitOption.FOLLOW_LINKS)
                        .parallel()
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(configuration.sqlFilesSuffix()))
                        .peek(path -> logger.debug(ApplicationEvents.CONSIDER_FILE, path));
            } catch (final IOException | SecurityException exception) {
                errors.add(exception);
            }
        }

        return Stream.empty();
    }

}
