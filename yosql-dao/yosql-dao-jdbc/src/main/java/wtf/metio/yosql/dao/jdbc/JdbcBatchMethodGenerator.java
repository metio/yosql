/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.api.BatchMethodGenerator;
import wtf.metio.yosql.codegen.api.ControlFlows;
import wtf.metio.yosql.codegen.api.Methods;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

public final class JdbcBatchMethodGenerator implements BatchMethodGenerator {

    private final ControlFlows controlFlow;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;
    private final JdbcTransformer transformer;

    public JdbcBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer transformer) {
        this.logging = logging;
        this.jdbc = jdbc;
        this.transformer = transformer;
        this.controlFlow = controlFlow;
        this.methods = methods;
        this.parameters = parameters;
    }

    @Override
    public MethodSpec batchWriteMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        return methods.batchMethod(configuration.batchName(), statements)
                .returns(TypicalTypes.ARRAY_OF_INTS)
                .addParameters(parameters.asBatchParameterSpecs(configuration.parameters()))
                .addExceptions(transformer.sqlException(configuration))
                .addCode(logging.entering(configuration.repository(), configuration.batchName()))
                .addCode(jdbc.openConnection())
                .addCode(jdbc.pickVendorQuery(statements))
                .addCode(jdbc.createStatement())
                .addCode(jdbc.prepareBatch(configuration))
                .addCode(jdbc.logExecutedBatchQuery(configuration))
                .addCode(jdbc.executeBatch())
                .addCode(controlFlow.endTryBlock(2))
                .addCode(controlFlow.maybeCatchAndRethrow(configuration))
                .build();
    }

}
