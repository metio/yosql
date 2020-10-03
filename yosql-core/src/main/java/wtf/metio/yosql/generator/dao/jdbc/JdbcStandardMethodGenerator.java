/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao.jdbc;

import com.squareup.javapoet.MethodSpec;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.api.StandardMethodGenerator;
import wtf.metio.yosql.generator.blocks.api.ControlFlows;
import wtf.metio.yosql.generator.blocks.api.Methods;
import wtf.metio.yosql.generator.blocks.api.Parameters;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcBlocks;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcTransformer;
import wtf.metio.yosql.generator.helpers.TypicalTypes;
import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;

final class JdbcStandardMethodGenerator implements StandardMethodGenerator {

    private final ControlFlows controlFlows;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;
    private final JdbcTransformer jdbcTransformer;

    JdbcStandardMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer jdbcTransformer) {
        this.logging = logging;
        this.jdbc = jdbc;
        this.jdbcTransformer = jdbcTransformer;
        this.controlFlows = controlFlows;
        this.methods = methods;
        this.parameters = parameters;
    }

    @Override
    public MethodSpec standardReadMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = configuration.getResultRowConverter();
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var listOfResults = TypicalTypes.listOf(resultType);
        final var methodName = configuration.getName();
        return methods.publicMethod(methodName, statements)
                .returns(listOfResults)
                .addParameters(parameters.asParameterSpecs(configuration.getParameters()))
                .addExceptions(jdbcTransformer.sqlException(configuration))
                .addCode(logging.entering(configuration.getRepository(), methodName))
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement())
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

    @Override
    public MethodSpec standardWriteMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var methodName = configuration.getName();
        return methods.publicMethod(methodName, statements)
                .returns(int.class)
                .addExceptions(jdbcTransformer.sqlException(configuration))
                .addParameters(parameters.asParameterSpecs(configuration.getParameters()))
                .addCode(logging.entering(configuration.getRepository(), methodName))
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement())
                .addCode(jdbc.setParameters(configuration))
                .addCode(jdbc.logExecutedQuery(configuration))
                .addCode(jdbc.executeUpdate())
                .addCode(controlFlows.endTryBlock(2))
                .addCode(controlFlows.maybeCatchAndRethrow(configuration))
                .build();
    }

    @Override
    public MethodSpec standardCallMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = configuration.getResultRowConverter();
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var listOfResults = TypicalTypes.listOf(resultType);
        final var methodName = configuration.getName();
        return methods.publicMethod(methodName, statements)
                .returns(listOfResults)
                .addParameters(parameters.asParameterSpecs(configuration.getParameters()))
                .addExceptions(jdbcTransformer.sqlException(configuration))
                .addCode(logging.entering(configuration.getRepository(), methodName))
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
