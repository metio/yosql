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
    private final MethodAssembly assembly;

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
        this.assembly = new MethodAssembly(controlFlows, methods, logging, jdbc, exceptions);
    }

    @Override
    public MethodSpec readMethodDeclaration(final SqlConfiguration configuration, final List<SqlStatement> statements) {
        final var builder = methods.declaration(configuration.executeOnceName(), statements, Constants.EXECUTE_ONCE)
                .addParameters(parameters.asParameterSpecsForInterfaces(configuration))
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
        final var name = configuration.executeOnceName();
        final var builder = assembly
                .start(configuration, statements, name, Constants.EXECUTE_ONCE,
                        parameters.asParameterSpecs(configuration));
        assembly.openConnection(builder, configuration, statements)
                .addCode(jdbc.createStatement(configuration))
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeStatement(configuration));
        // the statement and its result set
        return assembly.close(builder, configuration, 2);
    }

    private MethodSpec readSingle(
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
                .addCode(jdbc.executeStatement(configuration))
                .addCode(jdbc.returnAsSingle(configuration));
        // the statement and its result set
        return assembly.close(builder, configuration, 2);
    }

    private MethodSpec readMultiple(
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
                .addCode(jdbc.executeStatement(configuration))
                .addCode(jdbc.returnAsMultiple(converter));
        // the statement and its result set
        return assembly.close(builder, configuration, 2);
    }

    private MethodSpec readCursor(
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
                .addCode(jdbc.pickVendorQuery(statements))
                .addStatement(jdbc.prepareStatementInline())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeQueryStatement())
                .addCode(jdbc.streamStateful(configuration))
                .addCode(controlFlows.endMaybeTry(configuration))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

}
