/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao.jdbc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.generator.api.Java8StreamMethodGenerator;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.blocks.generic.*;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcBlocks;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcTransformer;
import wtf.metio.yosql.generator.helpers.TypicalTypes;
import wtf.metio.yosql.model.sql.ResultRowConverter;
import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;

public final class JdbcJava8StreamMethodGenerator implements Java8StreamMethodGenerator {

    private final GenericBlocks blocks;
    private final ControlFlows controlFlow;
    private final Names names;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbcBlocks;
    private final JdbcTransformer jdbcTransformer;

    public JdbcJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            final JdbcTransformer jdbcTransformer) {
        this.names = names;
        this.logging = logging;
        this.blocks = blocks;
        this.jdbcBlocks = jdbcBlocks;
        this.jdbcTransformer = jdbcTransformer;
        this.controlFlow = controlFlow;
        this.methods = methods;
        this.parameters = parameters;
    }

    @Override
    public MethodSpec streamEagerMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = configuration.getResultRowConverter();
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var listOfResults = TypicalTypes.listOf(resultType);
        final var streamOfResults = TypicalTypes.streamOf(resultType);
        return methods.publicMethod(configuration.getStreamEagerName(), statements)
                .returns(streamOfResults)
                .addParameters(parameters.asParameterSpecs(configuration.getParameters()))
                .addExceptions(jdbcTransformer.sqlException(configuration))
                .addCode(logging.entering(configuration.getRepository(), configuration.getStreamEagerName()))
                .addCode(jdbcBlocks.openConnection())
                .addCode(jdbcBlocks.pickVendorQuery(statements))
                .addCode(jdbcBlocks.createStatement())
                .addCode(jdbcBlocks.setParameters(configuration))
                .addCode(jdbcBlocks.logExecutedQuery(configuration))
                .addCode(jdbcBlocks.executeStatement())
                .addCode(jdbcBlocks.readMetaData())
                .addCode(jdbcBlocks.readColumnCount())
                .addCode(jdbcBlocks.createResultState())
                .addCode(jdbcBlocks.returnAsStream(listOfResults, converter.alias()))
                .addCode(controlFlow.endTryBlock(3))
                .addCode(controlFlow.maybeCatchAndRethrow(configuration))
                .build();
    }

    @Override
    public MethodSpec streamLazyMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = configuration.getResultRowConverter();
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var streamOfResults = TypicalTypes.streamOf(resultType);
        return methods.publicMethod(configuration.getStreamLazyName(), statements)
                .returns(streamOfResults)
                .addParameters(parameters.asParameterSpecs(configuration.getParameters()))
                .addExceptions(jdbcTransformer.sqlException(configuration))
                .addCode(logging.entering(configuration.getRepository(), configuration.getStreamLazyName()))
                .addCode(controlFlow.maybeTry(configuration))
                .addCode(jdbcBlocks.connectionVariable())
                .addCode(jdbcBlocks.pickVendorQuery(statements))
                .addCode(jdbcBlocks.statementVariable())
                .addCode(jdbcBlocks.setParameters(configuration))
                .addCode(jdbcBlocks.logExecutedQuery(configuration))
                .addCode(jdbcBlocks.resultSetVariableStatement())
                .addCode(jdbcBlocks.readMetaData())
                .addCode(jdbcBlocks.readColumnCount())
                .addCode(jdbcBlocks.createResultState())
                .addCode(jdbcBlocks.streamStateful(lazyStreamSpliterator(converter), lazyStreamCloser()))
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
                        .addCode(jdbcBlocks.closeResultSet())
                        .addCode(jdbcBlocks.closePrepareStatement())
                        .addCode(jdbcBlocks.closeConnection())
                        .addCode(controlFlow.endTryBlock())
                        .addCode(controlFlow.catchAndRethrow())
                        .build())
                .build();
    }

}
