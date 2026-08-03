/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.dao;

import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;
import wtf.metio.yosql.codegen.blocks.ControlFlows;
import wtf.metio.yosql.codegen.blocks.Methods;
import wtf.metio.yosql.codegen.exceptions.MissingRepositoryNameException;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * The parts every generated statement method has, wherever it came from.
 *
 * <p>Reading, writing and calling produce nine methods between them, and they differ in the middle:
 * which JDBC calls run, and what comes back. What surrounds that middle — the signature, the
 * exceptions, the entry log, opening the connection, closing what was opened — was written out nine
 * times, which is nine places for the parts to drift from each other.</p>
 *
 * <p>{@link #close(MethodSpec.Builder, SqlConfiguration, int)} is the one that matters. A caller
 * says how many flows its own body opened; how many opening the connection opened is not the
 * caller's business, and answering that from a fixed number is what made a statement that neither
 * opens a connection nor catches exceptions fail to generate at all.</p>
 */
final class MethodAssembly {

    private final ControlFlows controlFlows;
    private final Methods methods;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;
    private final MethodExceptionHandler exceptions;

    MethodAssembly(
            final ControlFlows controlFlows,
            final Methods methods,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final MethodExceptionHandler exceptions) {
        this.controlFlows = controlFlows;
        this.methods = methods;
        this.logging = logging;
        this.jdbc = jdbc;
        this.exceptions = exceptions;
    }

    /**
     * The signature, the exceptions it declares, and the line that logs entering it.
     */
    MethodSpec.Builder start(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements,
            final String name,
            final String constant,
            final Iterable<ParameterSpec> parameters) {
        return methods.publicMethod(name, statements, constant)
                .addParameters(parameters)
                .addExceptions(exceptions.thrownExceptions(configuration))
                .addCode(logging.entering(
                        configuration.repository().orElseThrow(MissingRepositoryNameException::new), name));
    }

    /**
     * Opens the connection the statement runs on and picks the statement its database wants.
     */
    MethodSpec.Builder openConnection(
            final MethodSpec.Builder builder,
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return builder
                .addCode(jdbc.openConnection(configuration))
                .addCode(jdbc.pickVendorQuery(statements));
    }

    /**
     * @param bodyFlows how many control flows the caller's own body opened. What opening the
     *                  connection opened is added here, because only this knows it.
     */
    MethodSpec close(
            final MethodSpec.Builder builder,
            final SqlConfiguration configuration,
            final int bodyFlows) {
        return builder
                .addCode(controlFlows.endTryBlock(bodyFlows + jdbc.connectionFlows(configuration)))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

}
