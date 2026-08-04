/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.dao;

import com.palantir.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.blocks.ControlFlows;
import wtf.metio.yosql.codegen.blocks.Methods;
import wtf.metio.yosql.codegen.exceptions.MissingRepositoryNameException;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.configuration.Constants;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.configuration.GeneratedNames;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

import static wtf.metio.yosql.models.configuration.ReturningMode.NONE;

public final class DefaultWriteMethodGenerator implements WriteMethodGenerator {

    private final ControlFlows controlFlows;
    private final Methods methods;
    private final ParameterGenerator parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;
    private final MethodExceptionHandler exceptions;
    private final ConverterConfiguration converters;
    private final ReturnTypes returnTypes;
    private final MethodAssembly assembly;

    public DefaultWriteMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final ParameterGenerator parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final MethodExceptionHandler exceptions,
            final ConverterConfiguration converters,
            final ReturnTypes returnTypes) {
        this.logging = logging;
        this.jdbc = jdbc;
        this.exceptions = exceptions;
        this.controlFlows = controlFlows;
        this.methods = methods;
        this.parameters = parameters;
        this.converters = converters;
        this.returnTypes = returnTypes;
        this.assembly = new MethodAssembly(controlFlows, methods, logging, jdbc, exceptions);
    }

    @Override
    public MethodSpec writeMethodDeclaration(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        final var builder = methods.declaration(configuration.executeOnceName(), statements, Constants.EXECUTE_ONCE)
                .addParameters(parameters.asParameterSpecsForInterfaces(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration));
        returnTypes.resultType(configuration).ifPresent(builder::returns);
        return builder.build();
    }

    @Override
    public MethodSpec writeMethod(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        return switch (configuration.returningMode().orElse(NONE)) {
            case NONE -> writeReturningNone(configuration, statements);
            case SINGLE -> writeReturningSingle(configuration, statements);
            case MULTIPLE -> writeReturningMultiple(configuration, statements);
            case CURSOR -> writeReturningCursor(configuration, statements);
        };
    }

    private MethodSpec writeReturningNone(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var name = configuration.executeOnceName();
        final var builder = assembly
                .start(configuration, statements, name, Constants.EXECUTE_ONCE,
                        parameters.asParameterSpecs(configuration))
                .returns(returnTypes.noneResultType(configuration));
        assembly.openConnection(builder, configuration, statements)
                .addCode(jdbc.createStatement(configuration))
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.returnExecuteUpdate(configuration));
        return assembly.close(builder, configuration, 1);
    }

    private MethodSpec writeReturningSingle(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var name = configuration.executeOnceName();
        final var builder = assembly
                .start(configuration, statements, name, Constants.EXECUTE_ONCE,
                        parameters.asParameterSpecs(configuration))
                .returns(returnTypes.singleResultType(configuration));
        assembly.openConnection(builder, configuration, statements)
                .addCode(jdbc.createStatement(configuration))
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addStatement(jdbc.executeForReturning(configuration))
                .addCode(jdbc.getResultSet())
                .addCode(jdbc.returnAsSingle(configuration));
        return assembly.close(builder, configuration, 2);
    }

    private MethodSpec writeReturningMultiple(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var name = configuration.executeOnceName();
        final var converter = configuration.converter(converters::defaultConverter);
        final var builder = assembly
                .start(configuration, statements, name, Constants.EXECUTE_ONCE,
                        parameters.asParameterSpecs(configuration))
                .returns(returnTypes.multiResultType(configuration));
        assembly.openConnection(builder, configuration, statements)
                .addCode(jdbc.createStatement(configuration))
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addStatement(jdbc.executeForReturning(configuration))
                .addCode(jdbc.getResultSet())
                .addCode(jdbc.returnAsMultiple(converter));
        return assembly.close(builder, configuration, 2);
    }

    private MethodSpec writeReturningCursor(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var name = configuration.executeOnceName();
        return methods.publicMethod(name, statements, Constants.EXECUTE_ONCE)
                .returns(returnTypes.cursorResultType(configuration))
                .addParameters(parameters.asParameterSpecs(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(MissingRepositoryNameException::new), name))
                .addCode(controlFlows.maybeTry(configuration))
                .addCode(jdbc.getConnection(configuration))
                .addCode(jdbc.openConnectionScope(configuration))
                .addCode(jdbc.pickVendorQuery(statements))
                .addStatement(jdbc.prepareStatementInline())
                .addCode(controlFlows.startTryBlock())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addStatement(jdbc.executeForReturning(configuration))
                .addCode(jdbc.getResultSetStatement())
                .addCode(controlFlows.startTryBlock())
                .addCode(jdbc.streamStateful(configuration))
                .addCode(controlFlows.closeOnFailure(GeneratedNames.RESULT_SET))
                .addCode(controlFlows.closeOnFailure(GeneratedNames.STATEMENT))
                .addCode(jdbc.closeConnectionOnFailure(configuration))
                .addCode(controlFlows.endMaybeTry(configuration))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    @Override
    public MethodSpec batchWriteMethodDeclaration(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        return methods.declaration(configuration.executeBatchName(), statements, Constants.EXECUTE_BATCH)
                .returns(TypicalTypes.ARRAY_OF_INTS)
                .addParameters(parameters.asBatchParameterSpecsForInterfaces(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .build();
    }

    @Override
    public MethodSpec batchWriteMethod(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        final var name = configuration.executeBatchName();
        final var builder = assembly
                .start(configuration, statements, name, Constants.EXECUTE_BATCH,
                        parameters.asBatchParameterSpecs(configuration))
                .returns(TypicalTypes.ARRAY_OF_INTS);
        assembly.openConnection(builder, configuration, statements)
                .addCode(jdbc.createStatement(configuration))
                .addCode(jdbc.prepareBatch(configuration))
                .addCode(jdbc.logExecutedBatchQuery(configuration))
                .addCode(jdbc.executeBatch());
        return assembly.close(builder, configuration, 1);
    }

}
