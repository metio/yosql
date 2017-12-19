/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
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
 * A SQL file resolver that starts at {@link ExecutionConfiguration#inputBaseDirectory() the given source}, walks into
 * every subdirectory and returns all .sql files.
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
        logger.trace(ApplicationEvents.READ_FILES, source);

        if (!errors.hasErrors()) {
            try {
                return Files.walk(source, FileVisitOption.FOLLOW_LINKS)
                        .parallel()
                        .peek(path -> logger.trace(ApplicationEvents.ENCOUNTER_FILE, path))
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(configuration.sqlFilesSuffix()))
                        .peek(path -> logger.trace(ApplicationEvents.CONSIDER_FILE, path));
            } catch (final IOException | SecurityException exception) {
                logger.error(ApplicationEvents.READ_FILES_FAILED, exception.getLocalizedMessage());
                errors.add(exception);
            }
        }

        return Stream.empty();
    }

}
