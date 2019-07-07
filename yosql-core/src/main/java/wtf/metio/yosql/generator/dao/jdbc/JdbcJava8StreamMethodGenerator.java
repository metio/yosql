/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao.jdbc;

import com.squareup.javapoet.*;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.model.ResultRowConverter;
import wtf.metio.yosql.model.SqlConfiguration;
import wtf.metio.yosql.model.SqlStatement;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.api.Java8StreamMethodGenerator;
import wtf.metio.yosql.generator.helpers.*;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;

import static wtf.metio.yosql.generator.helpers.TypicalJavadoc.methodJavadoc;

final class JdbcJava8StreamMethodGenerator implements Java8StreamMethodGenerator {

    private final TypicalCodeBlocks codeBlocks;
    private final AnnotationGenerator annotations;

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
                .addJavadoc(methodJavadoc(vendorStatements))
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
                .addJavadoc(methodJavadoc(vendorStatements))
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
