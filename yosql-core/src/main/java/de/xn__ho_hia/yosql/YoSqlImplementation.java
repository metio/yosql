/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql;

import static de.xn__ho_hia.yosql.model.ApplicationEvents.*;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import de.xn__ho_hia.yosql.generator.api.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.api.TypeWriter;
import de.xn__ho_hia.yosql.generator.api.UtilitiesGenerator;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.PackageTypeSpec;
import de.xn__ho_hia.yosql.model.SqlStatement;
import de.xn__ho_hia.yosql.model.Translator;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import de.xn__ho_hia.yosql.utils.Timer;

/**
 * Default implementation of YoSql.
 */
// TODO: rename to AsyncYoSql?
final class YoSqlImplementation implements YoSql {

    private final SqlFileResolver        fileResolver;
    private final SqlFileParser          sqlFileParser;
    // TODO: add interface combining RepositoryGenerator + UtilitiesGenerator?
    private final RepositoryGenerator    repositoryGenerator;
    private final UtilitiesGenerator     utilsGenerator;
    private final ExecutionErrors        errors;
    private final Timer                  timer;
    private final TypeWriter             typeWriter;
    private final Translator             translator;
    private final ExecutionConfiguration configuration;

    YoSqlImplementation(
            final SqlFileResolver fileResolver,
            final SqlFileParser sqlFileParser,
            final RepositoryGenerator repositoryGenerator,
            final UtilitiesGenerator utilsGenerator,
            final ExecutionErrors errors,
            final Timer timer,
            final TypeWriter typeWriter,
            final Translator translator,
            final ExecutionConfiguration configuration) {
        this.fileResolver = fileResolver;
        this.sqlFileParser = sqlFileParser;
        this.repositoryGenerator = repositoryGenerator;
        this.utilsGenerator = utilsGenerator;
        this.errors = errors;
        this.timer = timer;
        this.typeWriter = typeWriter;
        this.translator = translator;
        this.configuration = configuration;
    }

    @Override
    public void generateCode() {
        final Executor pool = createThreadPool();
        supplyAsync(this::parseFiles, pool)
                .thenApplyAsync(this::generateCode, pool)
                .thenAcceptAsync(this::writeIntoFiles, pool)
                .thenRunAsync(timer::printTimings, pool)
                .exceptionally(this::handleExceptions)
                .join();
        if (errors.hasErrors()) {
            errors.codeGenerationException(translator.nonLocalized(CODE_GENERATION_FAILED));
        }
    }

    private Executor createThreadPool() {
        // TODO: use 'ThreadPoolFactory' instead
        final ForkJoinWorkerThreadFactory factory = pool -> {
            final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName(translator.nonLocalized(WORKER_POOL_NAME, Integer.valueOf(worker.getPoolIndex())));
            return worker;
        };
        return new ForkJoinPool(calculateNumberOfThreadsToUse(), factory, null, false);
    }

    private int calculateNumberOfThreadsToUse() {
        final int threads = configuration.maxThreads();
        final int processors = Runtime.getRuntime().availableProcessors();
        return threads < 1 ? processors : Math.max(1, Math.min(threads, processors));
    }

    private List<SqlStatement> parseFiles() {
        final List<SqlStatement> statements = timer.timed(translator.nonLocalized(PARSE_FILES),
                () -> fileResolver.resolveFiles()
                        .flatMap(sqlFileParser::parse)
                        .collect(toList()));
        if (errors.hasErrors()) {
            errors.sqlFileParsingException(translator.nonLocalized(PARSE_FILES_FAILED));
        }
        return statements;
    }

    private List<PackageTypeSpec> generateCode(final List<SqlStatement> statements) {
        return Stream.concat(generateRepositories(statements), generateUtilities(statements))
                .collect(toList());
    }

    private Stream<PackageTypeSpec> generateRepositories(final List<SqlStatement> statements) {
        return timer.timed(translator.nonLocalized(GENERATE_REPOSITORIES),
                createTypeSpecs(statements, stmts -> stmts.stream()
                        .collect(groupingBy(SqlStatement::getRepository))
                        .entrySet()
                        .parallelStream()
                        .map(repository -> repositoryGenerator.generateRepository(
                                repository.getKey(), repository.getValue()))));
    }

    private Stream<PackageTypeSpec> generateUtilities(final List<SqlStatement> statements) {
        return timer.timed(translator.nonLocalized(GENERATE_UTILITIES),
                createTypeSpecs(statements, utilsGenerator::generateUtilities));
    }

    private static Supplier<Stream<PackageTypeSpec>> createTypeSpecs(
            final List<SqlStatement> statements,
            final Function<List<SqlStatement>, Stream<PackageTypeSpec>> creator) {
        return () -> listWithEntries(statements)
                .map(creator)
                .orElseGet(Stream::empty);
    }

    private void writeIntoFiles(final List<PackageTypeSpec> typeSpecs) {
        timer.timed(translator.nonLocalized(WRITE_FILES), writeTypeSpecs(typeSpecs));
    }

    private Runnable writeTypeSpecs(final List<PackageTypeSpec> typeSpecs) {
        return () -> listWithEntries(typeSpecs)
                .map(Collection::parallelStream)
                .ifPresent(stream -> stream
                        .filter(Objects::nonNull) // XXX: required?
                        .forEach(typeWriter::writeType));
    }

    private static <T> Optional<List<T>> listWithEntries(final List<T> value) {
        return Optional.ofNullable(value)
                .filter(list -> !list.isEmpty());
    }

    private Void handleExceptions(final Throwable throwable) {
        errors.add(throwable.getCause());
        return null;
    }

}
