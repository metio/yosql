/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.lifecycle.ApplicationErrors;
import wtf.metio.yosql.codegen.lifecycle.FileLifecycle;
import wtf.metio.yosql.codegen.orchestration.ExecutionErrors;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.util.List;

/**
 * Default SQL file parser that starts at {@link FilesConfiguration#inputBaseDirectory()
 * the given source}, walks into every subdirectory and returns all statements found in
 * {@link FilesConfiguration#sqlFilesSuffix() .sql files}.
 */
public final class DefaultFileParser implements FileParser {

    private final LocLogger logger;
    private final FilesConfiguration fileConfiguration;
    private final ExecutionErrors errors;
    private final SqlStatementParser statementParser;

    public DefaultFileParser(
            final LocLogger logger,
            final FilesConfiguration fileConfiguration,
            final ExecutionErrors errors,
            final SqlStatementParser statementParser) {
        this.logger = logger;
        this.fileConfiguration = fileConfiguration;
        this.errors = errors;
        this.statementParser = statementParser;
    }

    @Override
    public List<SqlStatement> parseFiles() {
        final var source = fileConfiguration.inputBaseDirectory();
        logger.trace(FileLifecycle.READ_FILES, source);

        try (final var files = Files.walk(source, FileVisitOption.FOLLOW_LINKS).parallel()) {
            return files.peek(path -> logger.trace(FileLifecycle.ENCOUNTER_FILE, path))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(fileConfiguration.sqlFilesSuffix()))
                    .peek(path -> logger.trace(FileLifecycle.CONSIDER_FILE, path))
                    .flatMap(statementParser::parse)
                    .toList();
        } catch (final IOException | SecurityException exception) {
            logger.error(ApplicationErrors.READ_FILES_FAILED, exception.getLocalizedMessage());
            errors.add(exception);
            return List.of();
        }
    }

}
