/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.dao.jdbc;

import java.util.List;

import javax.inject.Inject;

import com.squareup.javapoet.MethodSpec;

import de.xn__ho_hia.yosql.generator.api.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.api.BatchMethodGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalCodeBlocks;
import de.xn__ho_hia.yosql.generator.helpers.TypicalMethods;
import de.xn__ho_hia.yosql.generator.helpers.TypicalParameters;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;

@JDBC
final class JdbcBatchMethodGenerator implements BatchMethodGenerator {

    private final TypicalCodeBlocks   codeBlocks;
    private final AnnotationGenerator annotations;

    @Inject
    JdbcBatchMethodGenerator(
            final TypicalCodeBlocks codeBlocks,
            final AnnotationGenerator annotations) {
        this.codeBlocks = codeBlocks;
        this.annotations = annotations;
    }

    @Override
    public MethodSpec batchMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return TypicalMethods.publicMethod(mergedConfiguration.getBatchName())
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
