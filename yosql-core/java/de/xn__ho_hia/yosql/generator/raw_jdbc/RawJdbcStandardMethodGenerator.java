/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.raw_jdbc;

import java.util.List;

import javax.inject.Inject;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import de.xn__ho_hia.javapoet.TypeGuesser;
import de.xn__ho_hia.yosql.generator.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.StandardMethodGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalCodeBlocks;
import de.xn__ho_hia.yosql.generator.helpers.TypicalMethods;
import de.xn__ho_hia.yosql.generator.helpers.TypicalParameters;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;

@SuppressWarnings({ "javadoc" })
public class RawJdbcStandardMethodGenerator implements StandardMethodGenerator {

    private final TypicalCodeBlocks   codeBlocks;
    private final AnnotationGenerator annotations;

    @Inject
    public RawJdbcStandardMethodGenerator(
            final TypicalCodeBlocks codeBlocks,
            final AnnotationGenerator annotations) {
        this.codeBlocks = codeBlocks;
        this.annotations = annotations;
    }

    @Override
    public MethodSpec standardReadMethod(
            final String methodName,
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        final ResultRowConverter converter = mergedConfiguration.getResultRowConverter();
        final TypeName resultType = TypeGuesser.guessTypeName(converter.getResultType());
        final ParameterizedTypeName listOfResults = TypicalTypes.listOf(resultType);
        return TypicalMethods.publicMethod(methodName)
                .addAnnotations(annotations.generatedMethod(getClass()))
                .returns(listOfResults)
                .addParameters(TypicalParameters.asParameterSpecs(mergedConfiguration.getParameters()))
                .addExceptions(TypicalCodeBlocks.sqlException(mergedConfiguration))
                .addCode(codeBlocks.entering(mergedConfiguration.getRepository(), methodName))
                .addCode(TypicalCodeBlocks.tryConnect())
                .addCode(codeBlocks.pickVendorQuery(vendorStatements))
                .addCode(TypicalCodeBlocks.tryPrepareStatement())
                .addCode(TypicalCodeBlocks.setParameters(mergedConfiguration))
                .addCode(codeBlocks.logExecutedQuery(mergedConfiguration))
                .addCode(TypicalCodeBlocks.tryExecute())
                .addCode(TypicalCodeBlocks.getMetaData())
                .addCode(TypicalCodeBlocks.getColumnCount())
                .addCode(codeBlocks.newResultState())
                .addCode(TypicalCodeBlocks.returnAsList(listOfResults, converter.getAlias()))
                .addCode(TypicalCodeBlocks.endTryBlock(3))
                .addCode(TypicalCodeBlocks.maybeCatchAndRethrow(mergedConfiguration))
                .build();
    }

    @Override
    public MethodSpec standardWriteMethod(
            final String methodName,
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        return TypicalMethods.publicMethod(mergedConfiguration.getName())
                .addAnnotations(annotations.generatedMethod(getClass()))
                .returns(int.class)
                .addExceptions(TypicalCodeBlocks.sqlException(mergedConfiguration))
                .addParameters(TypicalParameters.asParameterSpecs(mergedConfiguration.getParameters()))
                .addCode(codeBlocks.entering(mergedConfiguration.getRepository(), methodName))
                .addCode(TypicalCodeBlocks.tryConnect())
                .addCode(codeBlocks.pickVendorQuery(vendorStatements))
                .addCode(TypicalCodeBlocks.tryPrepareStatement())
                .addCode(TypicalCodeBlocks.setParameters(mergedConfiguration))
                .addCode(codeBlocks.logExecutedQuery(mergedConfiguration))
                .addCode(TypicalCodeBlocks.executeUpdate())
                .addCode(TypicalCodeBlocks.endTryBlock(2))
                .addCode(TypicalCodeBlocks.maybeCatchAndRethrow(mergedConfiguration))
                .build();
    }

    @Override
    public MethodSpec standardCallMethod(final String methodName, final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> statements) {
        final ResultRowConverter converter = mergedConfiguration.getResultRowConverter();
        final TypeName resultType = TypeGuesser.guessTypeName(converter.getResultType());
        final ParameterizedTypeName listOfResults = TypicalTypes.listOf(resultType);
        return TypicalMethods.publicMethod(methodName)
                .addAnnotations(annotations.generatedMethod(getClass()))
                .returns(listOfResults)
                .addParameters(TypicalParameters.asParameterSpecs(mergedConfiguration.getParameters()))
                .addExceptions(TypicalCodeBlocks.sqlException(mergedConfiguration))
                .addCode(codeBlocks.entering(mergedConfiguration.getRepository(), methodName))
                .addCode(TypicalCodeBlocks.tryConnect())
                .addCode(codeBlocks.pickVendorQuery(statements))
                .addCode(TypicalCodeBlocks.tryPrepareCallable())
                .addCode(TypicalCodeBlocks.setParameters(mergedConfiguration))
                .addCode(codeBlocks.logExecutedQuery(mergedConfiguration))
                .addCode(TypicalCodeBlocks.tryExecute())
                .addCode(TypicalCodeBlocks.getMetaData())
                .addCode(TypicalCodeBlocks.getColumnCount())
                .addCode(codeBlocks.newResultState())
                .addCode(TypicalCodeBlocks.returnAsList(listOfResults, converter.getAlias()))
                .addCode(TypicalCodeBlocks.endTryBlock(3))
                .addCode(TypicalCodeBlocks.maybeCatchAndRethrow(mergedConfiguration))
                .build();
    }

}
