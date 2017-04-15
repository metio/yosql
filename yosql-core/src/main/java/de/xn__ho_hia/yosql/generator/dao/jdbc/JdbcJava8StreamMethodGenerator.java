/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.dao.jdbc;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;

import javax.inject.Inject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import de.xn__ho_hia.javapoet.TypeGuesser;
import de.xn__ho_hia.yosql.generator.api.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.api.Java8StreamMethodGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalCodeBlocks;
import de.xn__ho_hia.yosql.generator.helpers.TypicalMethods;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalParameters;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;

@JDBC
@SuppressWarnings("nls")
final class JdbcJava8StreamMethodGenerator implements Java8StreamMethodGenerator {

    private final TypicalCodeBlocks   codeBlocks;
    private final AnnotationGenerator annotations;

    @Inject
    JdbcJava8StreamMethodGenerator(
            final TypicalCodeBlocks codeBlocks,
            final AnnotationGenerator annotations) {
        this.codeBlocks = codeBlocks;
        this.annotations = annotations;
    }

    @Override
    public MethodSpec streamEagerMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> vendorStatements) {
        final ResultRowConverter converter = configuration.getResultRowConverter();
        final TypeName resultType = TypeGuesser.guessTypeName(converter.getResultType());
        final ParameterizedTypeName listOfResults = TypicalTypes.listOf(resultType);
        final ParameterizedTypeName streamOfResults = TypicalTypes.streamOf(resultType);
        return TypicalMethods.publicMethod(configuration.getStreamEagerName())
                .addAnnotations(annotations.generatedMethod(getClass()))
                .returns(streamOfResults)
                .addParameters(TypicalParameters.asParameterSpecs(configuration.getParameters()))
                .addExceptions(TypicalCodeBlocks.sqlException(configuration))
                .addCode(codeBlocks.entering(configuration.getRepository(),
                        configuration.getStreamEagerName()))
                .addCode(TypicalCodeBlocks.tryConnect())
                .addCode(codeBlocks.pickVendorQuery(vendorStatements))
                .addCode(TypicalCodeBlocks.tryPrepareStatement())
                .addCode(TypicalCodeBlocks.setParameters(configuration))
                .addCode(codeBlocks.logExecutedQuery(configuration))
                .addCode(TypicalCodeBlocks.tryExecute())
                .addCode(TypicalCodeBlocks.getMetaData())
                .addCode(TypicalCodeBlocks.getColumnCount())
                .addCode(codeBlocks.newResultState())
                .addCode(TypicalCodeBlocks.returnAsStream(listOfResults, converter.getAlias()))
                .addCode(TypicalCodeBlocks.endTryBlock(3))
                .addCode(TypicalCodeBlocks.maybeCatchAndRethrow(configuration))
                .build();
    }

    @Override
    public MethodSpec streamLazyMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        final ResultRowConverter converter = mergedConfiguration.getResultRowConverter();
        final TypeName resultType = TypeGuesser.guessTypeName(converter.getResultType());
        final ParameterizedTypeName streamOfResults = TypicalTypes.streamOf(resultType);
        return TypicalMethods.publicMethod(mergedConfiguration.getStreamLazyName())
                .addAnnotations(annotations.generatedMethod(getClass()))
                .returns(streamOfResults)
                .addParameters(TypicalParameters.asParameterSpecs(mergedConfiguration.getParameters()))
                .addExceptions(TypicalCodeBlocks.sqlException(mergedConfiguration))
                .addCode(codeBlocks.entering(mergedConfiguration.getRepository(),
                        mergedConfiguration.getStreamLazyName()))
                .addCode(TypicalCodeBlocks.maybeTry(mergedConfiguration))
                .addCode(TypicalCodeBlocks.getConnection())
                .addCode(codeBlocks.pickVendorQuery(vendorStatements))
                .addCode(TypicalCodeBlocks.prepareStatement())
                .addCode(TypicalCodeBlocks.setParameters(mergedConfiguration))
                .addCode(codeBlocks.logExecutedQuery(mergedConfiguration))
                .addCode(TypicalCodeBlocks.executeQuery())
                .addCode(TypicalCodeBlocks.getMetaData())
                .addCode(TypicalCodeBlocks.getColumnCount())
                .addCode(codeBlocks.newResultState())
                .addCode(TypicalCodeBlocks.streamStatefull(lazyStreamSpliterator(converter), lazyStreamCloser()))
                .addCode(TypicalCodeBlocks.endMaybeTry(mergedConfiguration))
                .addCode(TypicalCodeBlocks.maybeCatchAndRethrow(mergedConfiguration))
                .build();
    }

    private TypeSpec lazyStreamSpliterator(final ResultRowConverter converter) {
        final ClassName spliteratorClass = ClassName.get(Spliterators.AbstractSpliterator.class);
        final TypeName resultType = TypeGuesser.guessTypeName(converter.getResultType());
        final ParameterizedTypeName superinterface = ParameterizedTypeName.get(spliteratorClass, resultType);
        final ParameterizedTypeName consumerType = TypicalTypes.consumerOf(resultType);
        return TypeSpec
                .anonymousClassBuilder("$T.MAX_VALUE, $T.ORDERED", Long.class, Spliterator.class)
                .addSuperinterface(superinterface)
                .addMethod(TypicalMethods.implementation("tryAdvance")
                        .addAnnotations(annotations.generatedMethod(getClass()))
                        .addParameter(TypicalParameters.parameter(consumerType, TypicalNames.ACTION))
                        .returns(boolean.class)
                        .addCode(TypicalCodeBlocks.startTryBlock())
                        .addCode(TypicalCodeBlocks.ifHasNext())
                        .addStatement("$N.accept($N.asUserType($N))", TypicalNames.ACTION, converter.getAlias(),
                                TypicalNames.STATE)
                        .addCode(TypicalCodeBlocks.returnTrue())
                        .addCode(TypicalCodeBlocks.endIf())
                        .addCode(TypicalCodeBlocks.returnFalse())
                        .addCode(TypicalCodeBlocks.endTryBlock())
                        .addCode(TypicalCodeBlocks.catchAndRethrow())
                        .build())
                .build();
    }

    private TypeSpec lazyStreamCloser() {
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(TypicalMethods.implementation("run")
                        .addAnnotations(annotations.generatedMethod(getClass()))
                        .returns(void.class)
                        .addCode(TypicalCodeBlocks.startTryBlock())
                        .addCode(TypicalCodeBlocks.closeResultSet())
                        .addCode(TypicalCodeBlocks.closePrepareStatement())
                        .addCode(TypicalCodeBlocks.closeConnection())
                        .addCode(TypicalCodeBlocks.endTryBlock())
                        .addCode(TypicalCodeBlocks.catchAndRethrow())
                        .build())
                .build();
    }

}
