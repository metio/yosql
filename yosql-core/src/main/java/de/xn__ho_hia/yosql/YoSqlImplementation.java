/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import de.xn__ho_hia.yosql.generator.api.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.api.UtilitiesGenerator;
import de.xn__ho_hia.yosql.model.CodeGenerationException;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.SqlFileParsingException;
import de.xn__ho_hia.yosql.model.SqlStatement;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import de.xn__ho_hia.yosql.utils.Timer;

/**
 * Default implementation of YoSql.
 */
final class YoSqlImplementation implements YoSql {

    private final SqlFileResolver     fileResolver;
    private final SqlFileParser       sqlFileParser;
    private final RepositoryGenerator repositoryGenerator;
    private final UtilitiesGenerator  utilsGenerator;
    private final ExecutionErrors     errors;
    private final Timer               timer;

    @Inject
    YoSqlImplementation(
            final SqlFileResolver fileResolver,
            final SqlFileParser sqlFileParser,
            final RepositoryGenerator repositoryGenerator,
            final UtilitiesGenerator utilsGenerator,
            final ExecutionErrors errors,
            final Timer timer) {
        this.fileResolver = fileResolver;
        this.sqlFileParser = sqlFileParser;
        this.repositoryGenerator = repositoryGenerator;
        this.utilsGenerator = utilsGenerator;
        this.errors = errors;
        this.timer = timer;
    }

    @Override
    public void generateFiles() {
        final Executor executor = Executors.newWorkStealingPool();
        supplyAsync(this::parseFiles, executor)
                .thenAcceptAsync(this::generateCode, executor)
                .exceptionally(this::handleExceptions)
                .join();
        timer.printTimings();
        if (errors.hasErrors()) {
            errors.throwWith(new CodeGenerationException("Error during code generation")); //$NON-NLS-1$
        }
    }

    private Void handleExceptions(final Throwable throwable) {
        errors.add(throwable.getCause());
        return null;
    }

    private List<SqlStatement> parseFiles() {
        List<SqlStatement> statements = Collections.emptyList();
        statements = timer.timed("parse files", //$NON-NLS-1$
                () -> fileResolver.resolveFiles()
                        .flatMap(sqlFileParser::parse)
                        .collect(toList()));
        if (errors.hasErrors()) {
            errors.throwWith(new SqlFileParsingException("Error during SQL file parsing")); //$NON-NLS-1$
        }
        return statements;
    }

    private void generateCode(final List<SqlStatement> statements) {
        generateRepositories(statements);
        generateUtilities(statements);
    }

    private void generateRepositories(final List<SqlStatement> statements) {
        timer.timed("generate repositories", () -> { //$NON-NLS-1$
            if (statements != null && !statements.isEmpty()) {
                statements.stream()
                        .collect(groupingBy(SqlStatement::getRepository))
                        .entrySet()
                        .parallelStream()
                        .forEach(repository -> repositoryGenerator.generateRepository(repository.getKey(),
                                repository.getValue()));
            }
        });
    }

    private void generateUtilities(final List<SqlStatement> statements) {
        timer.timed("generate utilities", () -> { //$NON-NLS-1$
            if (statements != null && !statements.isEmpty()) {
                utilsGenerator.generateUtilities(statements);
            }
        });
    }

}
