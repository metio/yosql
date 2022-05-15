/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.blocks.CodeBlocks;
import wtf.metio.yosql.codegen.blocks.ControlFlows;
import wtf.metio.yosql.codegen.blocks.Methods;
import wtf.metio.yosql.codegen.blocks.Parameters;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;

public final class DefaultReadMethodGenerator implements ReadMethodGenerator {

    private final ControlFlows controlFlows;
    private final Methods methods;
    private final ParameterGenerator parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;
    private final MethodExceptionHandler exceptions;
    private final ConverterConfiguration converters;
    private final CodeBlocks blocks;
    private final NamesConfiguration names;
    private final Parameters params;

    public DefaultReadMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final ParameterGenerator parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final MethodExceptionHandler exceptions,
            final ConverterConfiguration converters,
            final CodeBlocks blocks,
            final NamesConfiguration names,
            final Parameters params) {
        this.logging = logging;
        this.jdbc = jdbc;
        this.exceptions = exceptions;
        this.controlFlows = controlFlows;
        this.methods = methods;
        this.parameters = parameters;
        this.converters = converters;
        this.blocks = blocks;
        this.names = names;
        this.params = params;
    }

    @Override
    public MethodSpec readMethod(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        return switch (configuration.returningMode()) {
            case NONE -> readNone(configuration, statements);
            case SINGLE -> readSingle(configuration, statements);
            case MULTIPLE -> readMultiple(configuration, statements);
            case CURSOR -> readCursor(configuration, statements);
        };
    }

    private MethodSpec readNone(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return methods.publicMethod(configuration.blockingName(), statements, "generateBlockingApi")
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository(), configuration.blockingName()))
                .addCode(controlFlows.maybeTry(configuration))
                .addStatement(jdbc.connectionVariable())
                .addCode(jdbc.pickVendorQuery(statements))
                .addStatement(jdbc.statementVariable())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement())
                .addCode(controlFlows.endMaybeTry(configuration))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec readSingle(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = configuration.converter(converters::defaultConverter);
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        return methods.publicMethod(configuration.blockingName(), statements, "generateBlockingApi")
                .returns(TypicalTypes.optionalOf(resultType))
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository(), configuration.blockingName()))
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement())
                .addCode(jdbc.returnAsSingle(resultType, converter))
                .addCode(controlFlows.endTryBlock(3))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec readMultiple(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = configuration.converter(converters::defaultConverter);
        final var resultType = TypicalTypes.listOf(TypeGuesser.guessTypeName(converter.resultType()));
        return methods.publicMethod(configuration.blockingName(), statements, "generateBlockingApi")
                .returns(resultType)
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository(), configuration.blockingName()))
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement())
                .addCode(jdbc.returnAsMultiple(resultType, converter))
                .addCode(controlFlows.endTryBlock(3))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec readCursor(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = configuration.converter(converters::defaultConverter);
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var streamOfResults = TypicalTypes.streamOf(resultType);
        return methods.publicMethod(configuration.blockingName(), statements, "generateBlockingApi")
                .returns(streamOfResults)
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository(), configuration.blockingName()))
                .addCode(controlFlows.maybeTry(configuration))
                .addStatement(jdbc.connectionVariable())
                .addCode(jdbc.pickVendorQuery(statements))
                .addStatement(jdbc.statementVariable())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.resultSetVariableStatement())
                .addCode(jdbc.streamStateful(lazyStreamSpliterator(converter), lazyStreamCloser()))
                .addCode(controlFlows.endMaybeTry(configuration))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    private TypeSpec lazyStreamSpliterator(final ResultRowConverter converter) {
        final var spliteratorClass = ClassName.get(Spliterators.AbstractSpliterator.class);
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var superinterface = ParameterizedTypeName.get(spliteratorClass, resultType);
        final var consumerType = TypicalTypes.consumerOf(resultType);
        return TypeSpec
                .anonymousClassBuilder("$T.MAX_VALUE, $T.ORDERED", Long.class, Spliterator.class)
                .addSuperinterface(superinterface)
                .addMethod(methods.implementation("tryAdvance")
                        .addParameter(params.parameter(consumerType, names.action()))
                        .returns(boolean.class)
                        .addCode(controlFlows.startTryBlock())
                        .addCode(controlFlows.ifHasNext())
                        .addStatement("$N.accept($N.$N($N))", names.action(), converter.alias(), converter.methodName(),
                                names.resultSet())
                        .addCode(blocks.returnTrue())
                        .addCode(controlFlows.endIf())
                        .addCode(blocks.returnFalse())
                        .addCode(controlFlows.endTryBlock())
                        .addCode(controlFlows.catchAndRethrow())
                        .build())
                .build();
    }

    private TypeSpec lazyStreamCloser() {
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(methods.implementation("run")
                        .returns(void.class)
                        .addCode(controlFlows.startTryBlock())
                        .addCode(jdbc.closeResultSet())
                        .addCode(jdbc.closePrepareStatement())
                        .addCode(jdbc.closeConnection())
                        .addCode(controlFlows.endTryBlock())
                        .addCode(controlFlows.catchAndRethrow())
                        .build())
                .build();
    }

}
