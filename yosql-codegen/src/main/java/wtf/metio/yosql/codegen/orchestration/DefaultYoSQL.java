/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.orchestration;

import ch.qos.cal10n.IMessageConveyor;
import wtf.metio.yosql.codegen.dao.CodeGenerator;
import wtf.metio.yosql.codegen.files.FileParser;
import wtf.metio.yosql.codegen.lifecycle.*;
import wtf.metio.yosql.codegen.validation.RuntimeValidator;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Default implementation of YoSQL. It's responsible for the high-level orchestration of a single code generation run
 * (which can be called multiple times if desired). Delegates most of the actual work to various interfaces whose
 * implementation must be provided at runtime.
 */
public final class DefaultYoSQL implements YoSQL {

    private final FileParser fileParser;
    private final CodeGenerator codeGenerator;
    private final Executor pool;
    private final Timer timer;
    private final IMessageConveyor messages;
    private final TypeWriter typeWriter;
    private final ExecutionErrors errors;
    private final RuntimeValidator validator;

    public DefaultYoSQL(
            final FileParser fileParser,
            final CodeGenerator codeGenerator,
            final Executor pool,
            final Timer timer,
            final IMessageConveyor messages,
            final TypeWriter typeWriter,
            final ExecutionErrors errors,
            final RuntimeValidator validator) {
        this.fileParser = fileParser;
        this.codeGenerator = codeGenerator;
        this.pool = pool;
        this.timer = timer;
        this.messages = messages;
        this.typeWriter = typeWriter;
        this.errors = errors;
        this.validator = validator;
    }

    @Override
    public void generateCode() {
        timer.timed(messages.getMessage(ValidationLifecycle.VALIDATE_CONFIGURATION), validator::validate);
        if (errors.hasErrors()) {
            errors.runtimeException(messages.getMessage(ApplicationErrors.RUNTIME_INVALID));
        }
        supplyAsync(this::parseFiles, pool)
                .thenApplyAsync(this::timeCodeGeneration, pool)
                .thenAcceptAsync(this::writeIntoFiles, pool)
                .thenRunAsync(timer::printTimings, pool)
                .exceptionally(this::handleExceptions)
                .join();
        if (errors.hasErrors()) {
            errors.codeGenerationException(messages.getMessage(ApplicationErrors.CODE_GENERATION_FAILED));
        }
    }

    private List<SqlStatement> parseFiles() {
        final var statements = timer.timed(messages.getMessage(ParseLifecycle.PARSE_FILES),
                fileParser::parseFiles);
        if (errors.hasErrors()) {
            errors.sqlFileParsingException(messages.getMessage(ApplicationErrors.PARSE_FILES_FAILED));
        }
        return statements;
    }

    private Stream<PackagedTypeSpec> timeCodeGeneration(
            final List<SqlStatement> statements) {
        return timer.timed(messages.getMessage(CodegenLifecycle.GENERATE_REPOSITORIES),
                () -> codeGenerator.generateCode(statements));
    }

    private void writeIntoFiles(final Stream<PackagedTypeSpec> typeSpecs) {
        timer.timed(messages.getMessage(WriteLifecycle.WRITE_FILES),
                () -> typeSpecs.parallel().forEach(typeWriter::writeType));
    }

    private Void handleExceptions(final Throwable throwable) {
        errors.add(Objects.requireNonNullElse(throwable.getCause(), throwable));
        return null;
    }

}
