/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.model.configuration.FileConfiguration;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.errors.ExecutionErrors;
import wtf.metio.yosql.model.internal.ApplicationEvents;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * A SQL file resolver that starts at {@link FileConfiguration#inputBaseDirectory()
 * the given source}, walks into every subdirectory and returns all .sql files.
 */
final class DefaultSqlFileResolver implements SqlFileResolver {

    private final LocLogger logger;
    private final ParserPreconditions preconditions;
    // TODO: inject inputBaseDirectory instead
    //       allows to use this class w/o RuntimeConfiguration
    private final RuntimeConfiguration configuration;
    private final ExecutionErrors errors;

    DefaultSqlFileResolver(
            final LocLogger logger,
            final ParserPreconditions preconditions,
            final RuntimeConfiguration configuration,
            final ExecutionErrors errors) {
        this.logger = logger;
        this.preconditions = preconditions;
        this.configuration = configuration;
        this.errors = errors;
    }

    @Override
    public Stream<Path> resolveFiles() {
        final var source = configuration.files().inputBaseDirectory();
        logger.trace(ApplicationEvents.READ_FILES, source);
        preconditions.assertDirectoryIsReadable(source);

        if (!errors.hasErrors()) {
            try {
                return Files.walk(source, FileVisitOption.FOLLOW_LINKS)
                        .parallel()
                        .peek(path -> logger.trace(ApplicationEvents.ENCOUNTER_FILE, path))
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(configuration.files().sqlFilesSuffix()))
                        .peek(path -> logger.trace(ApplicationEvents.CONSIDER_FILE, path));
            } catch (final IOException | SecurityException exception) {
                // TODO: use 'errors.illegalState' or similar
                logger.error(ApplicationEvents.READ_FILES_FAILED, exception.getLocalizedMessage());
                errors.add(exception);
            }
        }

        return Stream.empty();
    }

}
