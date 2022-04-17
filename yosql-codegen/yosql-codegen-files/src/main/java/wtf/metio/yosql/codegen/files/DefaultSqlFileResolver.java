/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.errors.ExecutionErrors;
import wtf.metio.yosql.codegen.lifecycle.ApplicationErrors;
import wtf.metio.yosql.codegen.lifecycle.FileLifecycle;
import wtf.metio.yosql.models.immutables.FilesConfiguration;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * A SQL file resolver that starts at {@link FilesConfiguration#inputBaseDirectory()
 * the given source}, walks into every subdirectory and returns all .sql files.
 */
public final class DefaultSqlFileResolver implements SqlFileResolver {

    private final LocLogger logger;
    private final ParserPreconditions preconditions;
    private final FilesConfiguration fileConfiguration;
    private final ExecutionErrors errors;

    public DefaultSqlFileResolver(
            final LocLogger logger,
            final ParserPreconditions preconditions,
            final FilesConfiguration fileConfiguration,
            final ExecutionErrors errors) {
        this.logger = logger;
        this.preconditions = preconditions;
        this.fileConfiguration = fileConfiguration;
        this.errors = errors;
    }

    @Override
    public Stream<Path> resolveFiles() {
        final var source = fileConfiguration.inputBaseDirectory();
        logger.trace(FileLifecycle.READ_FILES, source);
        preconditions.assertDirectoryIsReadable(source);

        if (!errors.hasErrors()) {
            try {
                return Files.walk(source, FileVisitOption.FOLLOW_LINKS)
                        .parallel()
                        .peek(path -> logger.trace(FileLifecycle.ENCOUNTER_FILE, path))
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(fileConfiguration.sqlFilesSuffix()))
                        .peek(path -> logger.trace(FileLifecycle.CONSIDER_FILE, path));
            } catch (final IOException | SecurityException exception) {
                logger.error(ApplicationErrors.READ_FILES_FAILED, exception.getLocalizedMessage());
                errors.add(exception);
            }
        }

        return Stream.empty();
    }

}
