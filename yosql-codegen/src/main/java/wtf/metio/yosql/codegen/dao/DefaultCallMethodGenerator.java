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
import wtf.metio.yosql.models.configuration.Constants;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

import static wtf.metio.yosql.models.configuration.ReturningMode.NONE;

public final class DefaultCallMethodGenerator implements CallMethodGenerator {

    private final ControlFlows controlFlows;
    private final Methods methods;
    private final ParameterGenerator parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;
    private final MethodExceptionHandler exceptions;
    private final ConverterConfiguration converters;
    private final ReturnTypes returnTypes;

    public DefaultCallMethodGenerator(
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
    public MethodSpec callMethodDeclaration(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        final var builder = methods.declaration(configuration.executeOnceName(), statements, Constants.EXECUTE_ONCE)
                .addParameters(parameters.asParameterSpecsForInterfaces(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration));
        returnTypes.resultType(configuration).ifPresent(builder::returns);
        return builder.build();
    }

    @Override
    public MethodSpec callMethod(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        return switch (configuration.returningMode().orElse(NONE)) {
            case NONE -> callNone(configuration, statements);
            case SINGLE -> callSingle(configuration, statements);
            case MULTIPLE -> callMultiple(configuration, statements);
            case CURSOR -> callCursor(configuration, statements);
        };
    }

    private MethodSpec callNone(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        final var name = configuration.executeOnceName();
        return methods.publicMethod(name, statements, Constants.EXECUTE_ONCE)
                .addParameters(parameters.asParameterSpecs(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(MissingRepositoryNameException::new), name))
                .addCode(jdbc.openConnection(configuration))
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.tryPrepareCallable())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement(configuration))
                .addCode(controlFlows.endMaybeTry(configuration))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec callSingle(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        final var name = configuration.executeOnceName();
        return methods.publicMethod(name, statements, Constants.EXECUTE_ONCE)
                .returns(returnTypes.singleResultType(configuration))
                .addParameters(parameters.asParameterSpecs(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(MissingRepositoryNameException::new), name))
                .addCode(jdbc.openConnection(configuration))
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.tryPrepareCallable())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement(configuration))
                .addCode(jdbc.returnAsSingle(configuration))
                .addCode(controlFlows.endTryBlock(3))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec callMultiple(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        final var name = configuration.executeOnceName();
        final var converter = configuration.converter(converters::defaultConverter);
        return methods.publicMethod(name, statements, Constants.EXECUTE_ONCE)
                .returns(returnTypes.multiResultType(configuration))
                .addParameters(parameters.asParameterSpecs(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(MissingRepositoryNameException::new), name))
                .addCode(jdbc.openConnection(configuration))
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.tryPrepareCallable())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement(configuration))
                .addCode(jdbc.returnAsMultiple(converter))
                .addCode(controlFlows.endTryBlock(3))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec callCursor(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        final var name = configuration.executeOnceName();
        return methods.publicMethod(name, statements, Constants.EXECUTE_ONCE)
                .returns(returnTypes.cursorResultType(configuration))
                .addParameters(parameters.asParameterSpecs(configuration))
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(configuration.repository().orElseThrow(MissingRepositoryNameException::new), name))
                .addCode(jdbc.openConnection(configuration))
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.tryPrepareCallable())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement(configuration))
                .addCode(jdbc.streamStateful(configuration))
                .addCode(controlFlows.endMaybeTry(configuration))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

}
