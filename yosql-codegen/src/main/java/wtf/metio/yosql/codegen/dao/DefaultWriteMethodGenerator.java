/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.blocks.ControlFlows;
import wtf.metio.yosql.codegen.blocks.Methods;
import wtf.metio.yosql.codegen.exceptions.MissingRepositoryNameException;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.configuration.Constants;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
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
        return methods.publicMethod(name, statements, Constants.EXECUTE_ONCE)
                .returns(returnTypes.noneResultType(configuration))
                .addParameters(parameters.asParameterSpecs(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(MissingRepositoryNameException::new), name))
                .addCode(jdbc.openConnection(configuration))
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement(configuration))
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.returnExecuteUpdate(configuration))
                .addCode(controlFlows.endTryBlock(2))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec writeReturningSingle(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var name = configuration.executeOnceName();
        return methods.publicMethod(name, statements, Constants.EXECUTE_ONCE)
                .returns(returnTypes.singleResultType(configuration))
                .addParameters(parameters.asParameterSpecs(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(MissingRepositoryNameException::new), name))
                .addCode(jdbc.openConnection(configuration))
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement(configuration))
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addStatement(jdbc.executeForReturning())
                .addCode(jdbc.getResultSet())
                .addCode(jdbc.returnAsSingle(configuration))
                .addCode(controlFlows.endTryBlock(3))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec writeReturningMultiple(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var name = configuration.executeOnceName();
        final var converter = configuration.converter(converters::defaultConverter);
        return methods.publicMethod(name, statements, Constants.EXECUTE_ONCE)
                .returns(returnTypes.multiResultType(configuration))
                .addParameters(parameters.asParameterSpecs(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(MissingRepositoryNameException::new), name))
                .addCode(jdbc.openConnection(configuration))
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement(configuration))
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addStatement(jdbc.executeForReturning())
                .addCode(jdbc.getResultSet())
                .addCode(jdbc.returnAsMultiple(converter))
                .addCode(controlFlows.endTryBlock(3))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec writeReturningCursor(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var name = configuration.executeOnceName();
        final var converter = configuration.converter(converters::defaultConverter);
        return methods.publicMethod(name, statements, Constants.EXECUTE_ONCE)
                .returns(returnTypes.cursorResultType(configuration))
                .addParameters(parameters.asParameterSpecs(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(MissingRepositoryNameException::new), name))
                .addCode(jdbc.openConnection(configuration))
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement(configuration))
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addStatement(jdbc.executeForReturning())
                .addCode(jdbc.executeQueryStatement())
                .addCode(jdbc.streamStateful(converter))
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
        return methods.publicMethod(name, statements, Constants.EXECUTE_BATCH)
                .returns(TypicalTypes.ARRAY_OF_INTS)
                .addParameters(parameters.asBatchParameterSpecs(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(MissingRepositoryNameException::new), name))
                .addCode(jdbc.openConnection(configuration))
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement(configuration))
                .addCode(jdbc.prepareBatch(configuration))
                .addCode(jdbc.logExecutedBatchQuery(configuration))
                .addCode(jdbc.executeBatch())
                .addCode(controlFlows.endTryBlock(2))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

}
