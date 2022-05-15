/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.orchestration;

import ch.qos.cal10n.IMessageConveyor;
import wtf.metio.yosql.codegen.lifecycle.*;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public final class DefaultOrchestrator implements Orchestrator {

    private final Executor pool;
    private final Timer timer;
    private final IMessageConveyor messages;
    private final TypeWriter typeWriter;
    private final ExecutionErrors errors;

    public DefaultOrchestrator(
            final Executor pool,
            final Timer timer,
            final IMessageConveyor messages,
            final TypeWriter typeWriter,
            final ExecutionErrors errors) {
        this.pool = pool;
        this.timer = timer;
        this.errors = errors;
        this.messages = messages;
        this.typeWriter = typeWriter;
    }

    @Override
    public void execute(
            final Supplier<List<SqlStatement>> parser,
            final Function<List<SqlStatement>, Stream<PackagedTypeSpec>> generateCode) {
        supplyAsync(() -> parseFiles(parser), pool)
                .thenApplyAsync((statements -> timeCodeGeneration(generateCode, statements)), pool)
                .thenAcceptAsync(this::writeIntoFiles, pool)
                .thenRunAsync(timer::printTimings, pool)
                .exceptionally(this::handleExceptions)
                .join();
        if (errors.hasErrors()) {
            errors.codeGenerationException(messages.getMessage(ApplicationErrors.CODE_GENERATION_FAILED));
        }
    }

    private List<SqlStatement> parseFiles(final Supplier<List<SqlStatement>> parser) {
        final var statements = timer.timed(messages.getMessage(ParseLifecycle.PARSE_FILES), parser);
        if (errors.hasErrors()) {
            errors.sqlFileParsingException(messages.getMessage(ApplicationErrors.PARSE_FILES_FAILED));
        }
        return statements;
    }

    private Stream<PackagedTypeSpec> timeCodeGeneration(
            final Function<List<SqlStatement>, Stream<PackagedTypeSpec>> generateCode,
            final List<SqlStatement> statements) {
        return timer.timed(messages.getMessage(CodegenLifecycle.GENERATE_REPOSITORIES), () -> generateCode.apply(statements));
    }

    private void writeIntoFiles(final Stream<PackagedTypeSpec> typeSpecs) {
        timer.timed(messages.getMessage(WriteLifecycle.WRITE_FILES), writeTypeSpecs(typeSpecs));
    }

    private Runnable writeTypeSpecs(final Stream<PackagedTypeSpec> typeSpecs) {
        return () -> typeSpecs.parallel().forEach(typeWriter::writeType);
    }

    private Void handleExceptions(final Throwable throwable) {
        errors.add(throwable.getCause());
        return null;
    }

}
