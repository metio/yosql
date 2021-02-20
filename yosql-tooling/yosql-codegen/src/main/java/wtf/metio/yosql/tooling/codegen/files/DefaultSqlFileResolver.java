/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.tooling.codegen.model.configuration.FileConfiguration;
import wtf.metio.yosql.tooling.codegen.model.errors.ExecutionErrors;
import wtf.metio.yosql.tooling.codegen.model.internal.ApplicationEvents;

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
    private final FileConfiguration fileConfiguration;
    private final ExecutionErrors errors;

    DefaultSqlFileResolver(
            final LocLogger logger,
            final ParserPreconditions preconditions,
            final FileConfiguration fileConfiguration,
            final ExecutionErrors errors) {
        this.logger = logger;
        this.preconditions = preconditions;
        this.fileConfiguration = fileConfiguration;
        this.errors = errors;
    }

    @Override
    public Stream<Path> resolveFiles() {
        final var source = fileConfiguration.inputBaseDirectory();
        logger.trace(ApplicationEvents.READ_FILES, source);
        preconditions.assertDirectoryIsReadable(source);

        if (!errors.hasErrors()) {
            try {
                return Files.walk(source, FileVisitOption.FOLLOW_LINKS)
                        .parallel()
                        .peek(path -> logger.trace(ApplicationEvents.ENCOUNTER_FILE, path))
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(fileConfiguration.sqlFilesSuffix()))
                        .peek(path -> logger.trace(ApplicationEvents.CONSIDER_FILE, path));
            } catch (final IOException | SecurityException exception) {
                logger.error(ApplicationEvents.READ_FILES_FAILED, exception.getLocalizedMessage());
                errors.add(exception);
            }
        }

        return Stream.empty();
    }

}
