/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.MethodSpec;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

public final class JdbcReactorMethodGenerator implements ReactorMethodGenerator {

    private final ConverterConfiguration converters;
    private final Methods methods;
    private final Parameters parameters;
    private final MethodExceptionHandler exceptions;
    private final ControlFlows controlFlow;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;

    public JdbcReactorMethodGenerator(
            final ConverterConfiguration converters,
            final Methods methods,
            final Parameters parameters,
            final MethodExceptionHandler exceptions,
            final ControlFlows controlFlow,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc) {
        this.converters = converters;
        this.methods = methods;
        this.parameters = parameters;
        this.exceptions = exceptions;
        this.controlFlow = controlFlow;
        this.logging = logging;
        this.jdbc = jdbc;
    }

    @Override
    public MethodSpec reactorCallMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }

    @Override
    public MethodSpec reactorReadMethod(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        final var converter = configuration.resultRowConverter()
                .or(converters::defaultConverter).orElseThrow();
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var listOfResults = TypicalTypes.listOf(resultType);
        final var fluxOfResults = TypicalTypes.fluxOf(resultType);
        return methods.reactorMethod(configuration.reactorName(), statements)
                .returns(fluxOfResults)
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository(), configuration.reactorName()))
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement())
                .addCode(jdbc.readMetaData())
                .addCode(jdbc.readColumnCount())
                .addCode(jdbc.createResultState())
                .addCode(jdbc.returnAsFlux(listOfResults, converter.alias()))
                .addCode(controlFlow.endTryBlock(3))
                .addCode(controlFlow.maybeCatchAndRethrow(configuration))
                .build();
    }

    @Override
    public MethodSpec reactorWriteMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }

}
