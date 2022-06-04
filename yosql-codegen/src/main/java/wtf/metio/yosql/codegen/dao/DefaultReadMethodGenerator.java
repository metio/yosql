/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.MethodSpec;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.blocks.ControlFlows;
import wtf.metio.yosql.codegen.blocks.Methods;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.models.configuration.Constants;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

import static wtf.metio.yosql.models.configuration.ReturningMode.NONE;

public final class DefaultReadMethodGenerator implements ReadMethodGenerator {

    private final ControlFlows controlFlows;
    private final Methods methods;
    private final ParameterGenerator parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;
    private final MethodExceptionHandler exceptions;
    private final ConverterConfiguration converters;
    private final ReturnTypes returnTypes;

    public DefaultReadMethodGenerator(
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
    public MethodSpec readMethodDeclaration(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        final var builder = methods.declaration(configuration.standardName(), statements, Constants.GENERATE_STANDARD_API)
                .addParameters(parameters.asParameterSpecsForInterfaces(configuration.parameters()))
                .addExceptions(exceptions.thrownExceptions(configuration));
        returnTypes.resultType(configuration).ifPresent(builder::returns);
        return builder.build();
    }

    @Override
    public MethodSpec readMethod(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        return switch (configuration.returningMode().orElse(NONE)) {
            case NONE -> readNone(configuration, statements);
            case SINGLE -> readSingle(configuration, statements);
            case MULTIPLE -> readMultiple(configuration, statements);
            case CURSOR -> readCursor(configuration, statements);
        };
    }

    private MethodSpec readNone(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return methods.publicMethod(configuration.standardName(), statements, Constants.GENERATE_STANDARD_API)
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(), configuration.standardName())) // TODO: add business exception here
                .addCode(controlFlows.maybeTry(configuration))
                .addStatement(jdbc.getConnectionInline())
                .addCode(jdbc.pickVendorQuery(statements))
                .addStatement(jdbc.prepareStatementInline())
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
        return methods.publicMethod(configuration.standardName(), statements, Constants.GENERATE_STANDARD_API)
                .returns(returnTypes.singleResultType(configuration))
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(), configuration.standardName())) // TODO: add business exception here
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
        final var listOfResults = returnTypes.multiResultType(configuration);
        return methods.publicMethod(configuration.standardName(), statements, Constants.GENERATE_STANDARD_API)
                .returns(listOfResults)
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(), configuration.standardName())) // TODO: add business exception here
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement())
                .addCode(jdbc.returnAsMultiple(listOfResults, converter)) // TODO: pass-in raw resultType and let returnAsMultiple create list-type
                .addCode(controlFlows.endTryBlock(3))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec readCursor(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = configuration.converter(converters::defaultConverter);
        return methods.publicMethod(configuration.standardName(), statements, Constants.GENERATE_STANDARD_API)
                .returns(returnTypes.cursorResultType(configuration))
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(), configuration.standardName())) // TODO: add business exception here
                .addCode(controlFlows.maybeTry(configuration))
                .addStatement(jdbc.getConnectionInline())
                .addCode(jdbc.pickVendorQuery(statements))
                .addStatement(jdbc.prepareStatementInline())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeQueryStatement())
                .addCode(jdbc.streamStateful(converter))
                .addCode(controlFlows.endMaybeTry(configuration))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

}
