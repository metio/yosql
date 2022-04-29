/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.api.BlockingMethodGenerator;
import wtf.metio.yosql.codegen.api.ControlFlows;
import wtf.metio.yosql.codegen.api.Methods;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.util.List;
import java.util.function.BiFunction;

public final class JdbcBlockingMethodGenerator implements BlockingMethodGenerator {

    private final ControlFlows controlFlows;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;
    private final JdbcTransformer jdbcTransformer;
    private final ConverterConfiguration converters;

    public JdbcBlockingMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer jdbcTransformer,
            final ConverterConfiguration converters) {
        this.logging = logging;
        this.jdbc = jdbc;
        this.jdbcTransformer = jdbcTransformer;
        this.controlFlows = controlFlows;
        this.methods = methods;
        this.parameters = parameters;
        this.converters = converters;
    }

    @Override
    public MethodSpec blockingReadMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = converter(configuration);
        // TODO: protect against blank resultType
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        return switch (configuration.returningMode()) {
            // TODO: support NONE as well? e.g. to just send a select to a database to verify connections?
            case ONE -> read(configuration, statements, resultType, jdbc::returnAsOne);
            case FIRST -> read(configuration, statements, resultType, jdbc::returnAsFirst);
            default -> read(configuration, statements, TypicalTypes.listOf(resultType), jdbc::returnAsList);
        };
    }

    private <T extends TypeName> MethodSpec read(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements,
            final T resultType,
            final BiFunction<T, String, CodeBlock> returner) {
        final var converter = converter(configuration);
        return methods.blockingMethod(configuration.blockingName(), statements)
                .returns(resultType)
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(jdbcTransformer.sqlException(configuration))
                .addCode(logging.entering(configuration.repository(), configuration.blockingName()))
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement())
                .addCode(jdbc.readMetaData())
                .addCode(jdbc.readColumnCount())
                .addCode(jdbc.createResultState())
                .addCode(returner.apply(resultType, converter.alias()))
                .addCode(controlFlows.endTryBlock(3))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    @Override
    public MethodSpec blockingWriteMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = converter(configuration);
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        return switch (configuration.returningMode()) {
            case ONE -> writeReturningOne(configuration, statements, converter, resultType);
            case FIRST -> writeReturningFirst(configuration, statements, converter, resultType);
            case LIST -> writeReturningList(configuration, statements, converter, resultType);
            default -> writeReturningNone(configuration, statements);
        };
    }

    // TODO: move method into SqlConfiguration
    private ResultRowConverter converter(final SqlConfiguration configuration) {
        return configuration.resultRowConverter()
                .or(converters::defaultConverter)
                .orElseThrow();
    }

    private MethodSpec writeReturningOne(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements,
            final ResultRowConverter converter,
            final TypeName resultType) {
        return write(configuration, statements, converter, resultType, jdbc::returnAsOne);
    }

    private MethodSpec writeReturningFirst(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements,
            final ResultRowConverter converter,
            final TypeName resultType) {
        return write(configuration, statements, converter, resultType, jdbc::returnAsFirst);
    }

    private MethodSpec writeReturningList(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements,
            final ResultRowConverter converter,
            final TypeName resultType) {
        final var listOfResults = TypicalTypes.listOf(resultType);
        return write(configuration, statements, converter, listOfResults, jdbc::returnAsList);
    }

    private <T extends TypeName> MethodSpec write(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements,
            final ResultRowConverter converter,
            final T resultType,
            final BiFunction<T, String, CodeBlock> returner) {
        return methods.blockingMethod(configuration.blockingName(), statements)
                .returns(resultType)
                .addExceptions(jdbcTransformer.sqlException(configuration))
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addCode(logging.entering(configuration.repository(), configuration.blockingName()))
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addStatement(jdbc.executeForReturning())
                .addCode(jdbc.getResultSet())
                .addCode(jdbc.readMetaData())
                .addCode(jdbc.readColumnCount())
                .addCode(jdbc.createResultState())
                .addCode(returner.apply(resultType, converter.alias()))
                .addCode(controlFlows.endTryBlock(3))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec writeReturningNone(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var methodName = configuration.name();
        return methods.blockingMethod(methodName, statements)
                .returns(int.class)
                .addExceptions(jdbcTransformer.sqlException(configuration))
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addCode(logging.entering(configuration.repository(), methodName))
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.returnExecuteUpdate())
                .addCode(controlFlows.endTryBlock(2))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    @Override
    public MethodSpec blockingCallMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = converter(configuration);
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var listOfResults = TypicalTypes.listOf(resultType);
        return methods.blockingMethod(configuration.blockingName(), statements)
                .returns(listOfResults)
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(jdbcTransformer.sqlException(configuration))
                .addCode(logging.entering(configuration.repository(), configuration.blockingName()))
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.tryPrepareCallable())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement())
                .addCode(jdbc.readMetaData())
                .addCode(jdbc.readColumnCount())
                .addCode(jdbc.createResultState())
                .addCode(jdbc.returnAsList(listOfResults, converter.alias()))
                .addCode(controlFlows.endTryBlock(3))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

}
