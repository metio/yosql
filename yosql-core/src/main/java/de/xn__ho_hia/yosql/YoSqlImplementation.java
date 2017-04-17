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
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.stream.Stream;

import javax.inject.Inject;

import de.xn__ho_hia.yosql.generator.api.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.api.TypeWriter;
import de.xn__ho_hia.yosql.generator.api.UtilitiesGenerator;
import de.xn__ho_hia.yosql.model.CodeGenerationException;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.PackageTypeSpec;
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
    private final TypeWriter          typeWriter;

    @Inject
    YoSqlImplementation(
            final SqlFileResolver fileResolver,
            final SqlFileParser sqlFileParser,
            final RepositoryGenerator repositoryGenerator,
            final UtilitiesGenerator utilsGenerator,
            final ExecutionErrors errors,
            final Timer timer,
            final TypeWriter typeWriter) {
        this.fileResolver = fileResolver;
        this.sqlFileParser = sqlFileParser;
        this.repositoryGenerator = repositoryGenerator;
        this.utilsGenerator = utilsGenerator;
        this.errors = errors;
        this.timer = timer;
        this.typeWriter = typeWriter;
    }

    @Override
    public void generateFiles() {
        final Executor pool = createThreadPool();
        supplyAsync(this::parseFiles, pool)
                .thenApplyAsync(this::generateCode, pool)
                .thenAcceptAsync(this::writeIntoFiles, pool)
                .thenRunAsync(timer::printTimings, pool)
                .exceptionally(this::handleExceptions)
                .join();
        if (errors.hasErrors()) {
            errors.throwWith(new CodeGenerationException("Error during code generation")); //$NON-NLS-1$
        }
    }

    private static Executor createThreadPool() {
        final ForkJoinWorkerThreadFactory factory = pool -> {
            final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName("yosql-worker-" + worker.getPoolIndex()); //$NON-NLS-1$
            return worker;
        };
        final ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors(), factory, null, false);
        return pool;
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

    private List<PackageTypeSpec> generateCode(final List<SqlStatement> statements) {
        return Stream.concat(generateRepositories(statements), generateUtilities(statements))
                .collect(toList());
    }

    private Stream<PackageTypeSpec> generateRepositories(final List<SqlStatement> statements) {
        return timer.timed("generate repositories", () -> { //$NON-NLS-1$
            if (statements != null && !statements.isEmpty()) {
                return statements.stream()
                        .collect(groupingBy(SqlStatement::getRepository))
                        .entrySet()
                        .parallelStream()
                        .map(repository -> repositoryGenerator.generateRepository(repository.getKey(),
                                repository.getValue()));
            }
            return Stream.empty();
        });
    }

    private Stream<PackageTypeSpec> generateUtilities(final List<SqlStatement> statements) {
        return timer.timed("generate utilities", () -> { //$NON-NLS-1$
            if (statements != null && !statements.isEmpty()) {
                return utilsGenerator.generateUtilities(statements);
            }
            return Stream.empty();
        });
    }

    private void writeIntoFiles(final List<PackageTypeSpec> statements) {
        timer.timed("write types", () -> { //$NON-NLS-1$
            if (statements != null && !statements.isEmpty()) {
                statements.parallelStream()
                        .filter(Objects::nonNull)
                        .forEach(typeWriter::writeType);
            }
        });
    }

}
