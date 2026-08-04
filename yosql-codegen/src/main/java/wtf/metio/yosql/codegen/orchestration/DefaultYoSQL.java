/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.orchestration;

import ch.qos.cal10n.IMessageConveyor;
import wtf.metio.yosql.codegen.dao.CodeGenerator;
import wtf.metio.yosql.codegen.files.FileParser;
import wtf.metio.yosql.codegen.lifecycle.*;
import wtf.metio.yosql.codegen.validation.RuntimeValidator;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.codegen.schema.SchemaValidator;
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
    private final SchemaValidator schemaValidator;

    public DefaultYoSQL(
            final FileParser fileParser,
            final CodeGenerator codeGenerator,
            final Executor pool,
            final Timer timer,
            final IMessageConveyor messages,
            final TypeWriter typeWriter,
            final ExecutionErrors errors,
            final RuntimeValidator validator,
            final SchemaValidator schemaValidator) {
        this.fileParser = fileParser;
        this.codeGenerator = codeGenerator;
        this.pool = pool;
        this.timer = timer;
        this.messages = messages;
        this.typeWriter = typeWriter;
        this.errors = errors;
        this.validator = validator;
        this.schemaValidator = schemaValidator;
    }

    @Override
    public void generateCode() {
        timer.timed(messages.getMessage(ValidationLifecycle.VALIDATE_CONFIGURATION), validator::validate);
        if (errors.hasErrors()) {
            errors.logErrors();
            errors.runtimeException(messages.getMessage(ApplicationErrors.RUNTIME_INVALID));
        }
        supplyAsync(this::parseFiles, pool)
                .thenApplyAsync(this::validateAgainstSchema, pool)
                .thenApplyAsync(this::generateCode, pool)
                .thenAcceptAsync(this::writeIntoFiles, pool)
                .thenRunAsync(timer::printTimings, pool)
                .exceptionally(this::handleExceptions)
                .join();
        if (errors.hasErrors()) {
            errors.logErrors();
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

    /**
     * Runs after parsing because the schema is read from the same files, and before generating
     * because a statement that disagrees with its schema should not reach the point of producing
     * code somebody compiles.
     */
    private List<SqlStatement> validateAgainstSchema(final List<SqlStatement> statements) {
        // Its own name, because the timings are a map keyed by it: sharing one with the
        // configuration pass dropped whichever ran first out of the report and out of the total.
        timer.timed(messages.getMessage(ValidationLifecycle.VALIDATE_SCHEMA),
                () -> schemaValidator.validate(statements));
        return statements;
    }

    private Stream<PackagedTypeSpec> generateCode(final List<SqlStatement> statements) {
        return timer.timed(messages.getMessage(CodegenLifecycle.GENERATE_REPOSITORIES),
                () -> codeGenerator.generateCode(statements));
    }

    private void writeIntoFiles(final Stream<PackagedTypeSpec> typeSpecs) {
        timer.timed(messages.getMessage(WriteLifecycle.WRITE_FILES), () -> {
            typeSpecs.parallel().forEach(typeWriter::writeType);
            // After writing rather than before, so that a run which fails part way through leaves
            // the previous answers alone rather than removing them and not replacing them.
            if (!errors.hasErrors()) {
                typeWriter.removeStaleOutput();
            }
        });
    }

    private Void handleExceptions(final Throwable throwable) {
        errors.add(Objects.requireNonNullElse(throwable.getCause(), throwable));
        return null;
    }

}
