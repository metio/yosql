/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.codegen.blocks.GenericBlocks;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;

public final class JdbcJava8StreamMethodGenerator implements Java8StreamMethodGenerator {

    private final JdbcConfiguration config;
    private final GenericBlocks blocks;
    private final ControlFlows controlFlow;
    private final Names names;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;
    private final JdbcTransformer transformer;

    public JdbcJava8StreamMethodGenerator(
            final JdbcConfiguration config,
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer transformer) {
        this.config = config;
        this.names = names;
        this.logging = logging;
        this.blocks = blocks;
        this.jdbc = jdbc;
        this.transformer = transformer;
        this.controlFlow = controlFlow;
        this.methods = methods;
        this.parameters = parameters;
    }

    @Override
    public MethodSpec streamEagerMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = configuration.resultRowConverter().orElse(config.defaultConverter().orElseThrow());
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var listOfResults = TypicalTypes.listOf(resultType);
        final var streamOfResults = TypicalTypes.streamOf(resultType);
        return methods.publicMethod(configuration.eagerName(), statements)
                .returns(streamOfResults)
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(transformer.sqlException(configuration))
                .addCode(logging.entering(configuration.repository(), configuration.streamEagerName()))
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement())
                .addCode(jdbc.readMetaData())
                .addCode(jdbc.readColumnCount())
                .addCode(jdbc.createResultState())
                .addCode(jdbc.returnAsStream(listOfResults, converter.alias()))
                .addCode(controlFlow.endTryBlock(3))
                .addCode(controlFlow.maybeCatchAndRethrow(configuration))
                .build();
    }

    @Override
    public MethodSpec streamLazyMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = configuration.resultRowConverter().orElse(config.defaultConverter().orElseThrow());
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var streamOfResults = TypicalTypes.streamOf(resultType);
        return methods.publicMethod(configuration.streamLazyName(), statements)
                .returns(streamOfResults)
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(transformer.sqlException(configuration))
                .addCode(logging.entering(configuration.repository(), configuration.streamLazyName()))
                .addCode(controlFlow.maybeTry(configuration))
                .addCode(jdbc.connectionVariable())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.statementVariable())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.resultSetVariableStatement())
                .addCode(jdbc.readMetaData())
                .addCode(jdbc.readColumnCount())
                .addCode(jdbc.createResultState())
                .addCode(jdbc.streamStateful(lazyStreamSpliterator(converter), lazyStreamCloser()))
                .addCode(controlFlow.endMaybeTry(configuration))
                .addCode(controlFlow.maybeCatchAndRethrow(configuration))
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
                        .addParameter(parameters.parameter(consumerType, names.action()))
                        .returns(boolean.class)
                        .addCode(controlFlow.startTryBlock())
                        .addCode(controlFlow.ifHasNext())
                        .addStatement("$N.accept($N.asUserType($N))", names.action(), converter.alias(), names.state())
                        .addCode(blocks.returnTrue())
                        .addCode(controlFlow.endIf())
                        .addCode(blocks.returnFalse())
                        .addCode(controlFlow.endTryBlock())
                        .addCode(controlFlow.catchAndRethrow())
                        .build())
                .build();
    }

    private TypeSpec lazyStreamCloser() {
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(methods.implementation("run")
                        .returns(void.class)
                        .addCode(controlFlow.startTryBlock())
                        .addCode(jdbc.closeResultSet())
                        .addCode(jdbc.closePrepareStatement())
                        .addCode(jdbc.closeConnection())
                        .addCode(controlFlow.endTryBlock())
                        .addCode(controlFlow.catchAndRethrow())
                        .build())
                .build();
    }

}
