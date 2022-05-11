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

public final class JdbcJava8StreamMethodGenerator implements Java8StreamMethodGenerator {

    private final ConverterConfiguration converters;
    private final ControlFlows controlFlow;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;
    private final MethodExceptionHandler exceptions;

    public JdbcJava8StreamMethodGenerator(
            final ConverterConfiguration converters,
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final MethodExceptionHandler exceptions) {
        this.converters = converters;
        this.logging = logging;
        this.jdbc = jdbc;
        this.exceptions = exceptions;
        this.controlFlow = controlFlow;
        this.methods = methods;
        this.parameters = parameters;
    }

    @Override
    public MethodSpec streamReadMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = configuration.resultRowConverter()
                .or(converters::defaultConverter).orElseThrow();
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var listOfResults = TypicalTypes.listOf(resultType);
        final var streamOfResults = TypicalTypes.streamOf(resultType);
        return methods.streamEagerMethod(configuration.streamName(), statements)
                .returns(streamOfResults)
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository(), configuration.streamName()))
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement())
                .addCode(jdbc.returnAsStream(listOfResults, converter))
                .addCode(controlFlow.endTryBlock(3))
                .addCode(controlFlow.maybeCatchAndRethrow(configuration))
                .build();
    }

}
