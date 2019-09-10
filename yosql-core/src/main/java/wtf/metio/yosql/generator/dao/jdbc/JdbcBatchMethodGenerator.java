/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao.jdbc;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.api.BatchMethodGenerator;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.blocks.api.Javadoc;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcBlocks;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcTransformer;
import wtf.metio.yosql.generator.blocks.api.ControlFlows;
import wtf.metio.yosql.generator.blocks.api.Methods;
import wtf.metio.yosql.generator.blocks.api.Parameters;
import wtf.metio.yosql.generator.helpers.TypicalTypes;
import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;

final class JdbcBatchMethodGenerator implements BatchMethodGenerator {

    private final ControlFlows controlFlow;
    private final AnnotationGenerator annotations;
    private final Javadoc javadoc;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;
    private final JdbcTransformer transformer;

    JdbcBatchMethodGenerator(
            final ControlFlows controlFlow,
            final AnnotationGenerator annotations,
            final Javadoc javadoc,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer transformer) {
        this.annotations = annotations;
        this.logging = logging;
        this.javadoc = javadoc;
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
        return methods.publicMethod(configuration.getBatchName())
                .addJavadoc(javadoc.methodJavadoc(statements))
                .addAnnotations(annotations.generatedMethod(getClass()))
                .returns(TypicalTypes.ARRAY_OF_INTS)
                .addParameters(parameters.asBatchParameterSpecs(configuration.getParameters()))
                .addExceptions(transformer.sqlException(configuration))
                .addCode(logging.entering(configuration.getRepository(), configuration.getBatchName()))
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
