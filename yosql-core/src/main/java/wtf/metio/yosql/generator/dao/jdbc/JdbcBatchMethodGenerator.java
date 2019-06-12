/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao.jdbc;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.model.SqlConfiguration;
import wtf.metio.yosql.model.SqlStatement;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.api.BatchMethodGenerator;
import wtf.metio.yosql.generator.helpers.TypicalCodeBlocks;
import wtf.metio.yosql.generator.helpers.TypicalMethods;
import wtf.metio.yosql.generator.helpers.TypicalParameters;
import wtf.metio.yosql.generator.helpers.TypicalTypes;

import java.util.List;

import static wtf.metio.yosql.generator.helpers.TypicalJavadoc.methodJavadoc;

final class JdbcBatchMethodGenerator implements BatchMethodGenerator {

    private final TypicalCodeBlocks codeBlocks;
    private final AnnotationGenerator annotations;

    JdbcBatchMethodGenerator(
            final TypicalCodeBlocks codeBlocks,
            final AnnotationGenerator annotations) {
        this.codeBlocks = codeBlocks;
        this.annotations = annotations;
    }

    @Override
    public MethodSpec batchWriteMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return TypicalMethods.publicMethod(mergedConfiguration.getBatchName())
                .addJavadoc(methodJavadoc(vendorStatements))
                .addAnnotations(annotations.generatedMethod(getClass()))
                .returns(TypicalTypes.ARRAY_OF_INTS)
                .addParameters(TypicalParameters.asBatchParameterSpecs(mergedConfiguration.getParameters()))
                .addExceptions(TypicalCodeBlocks.sqlException(mergedConfiguration))
                .addCode(codeBlocks.entering(mergedConfiguration.getRepository(), mergedConfiguration.getBatchName()))
                .addCode(TypicalCodeBlocks.tryConnect())
                .addCode(codeBlocks.pickVendorQuery(vendorStatements))
                .addCode(TypicalCodeBlocks.tryPrepareStatement())
                .addCode(TypicalCodeBlocks.prepareBatch(mergedConfiguration))
                .addCode(codeBlocks.logExecutedBatchQuery(mergedConfiguration))
                .addCode(TypicalCodeBlocks.executeBatch())
                .addCode(TypicalCodeBlocks.endTryBlock(2))
                .addCode(TypicalCodeBlocks.maybeCatchAndRethrow(mergedConfiguration))
                .build();
    }

}
