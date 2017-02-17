package com.github.sebhoss.yosql.generator.raw_jdbc;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.generator.AnnotationGenerator;
import com.github.sebhoss.yosql.generator.Java8StreamMethodGenerator;
import com.github.sebhoss.yosql.generator.helpers.TypicalCodeBlocks;
import com.github.sebhoss.yosql.generator.helpers.TypicalMethods;
import com.github.sebhoss.yosql.generator.helpers.TypicalNames;
import com.github.sebhoss.yosql.generator.helpers.TypicalParameters;
import com.github.sebhoss.yosql.generator.helpers.TypicalTypes;
import com.github.sebhoss.yosql.model.ResultRowConverter;
import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

@Named
@Singleton
public class RawJdbcJava8StreamMethodGenerator implements Java8StreamMethodGenerator {

    private final TypicalCodeBlocks   codeBlocks;
    private final AnnotationGenerator annotations;

    @Inject
    public RawJdbcJava8StreamMethodGenerator(
            final TypicalCodeBlocks codeBlocks,
            final AnnotationGenerator annotations) {
        this.codeBlocks = codeBlocks;
        this.annotations = annotations;
    }

    @Override
    public MethodSpec streamEagerMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        final ResultRowConverter converter = mergedConfiguration.getResultConverter();
        final TypeName resultType = TypicalTypes.guessTypeName(converter.getResultType());
        final ParameterizedTypeName listOfResults = TypicalTypes.listOf(resultType);
        final ParameterizedTypeName streamOfResults = TypicalTypes.streamOf(resultType);
        return TypicalMethods.publicMethod(mergedConfiguration.getStreamEagerName())
                .addAnnotations(annotations.generatedMethod(getClass()))
                .returns(streamOfResults)
                .addParameters(mergedConfiguration.getParameterSpecs())
                .addExceptions(TypicalCodeBlocks.sqlException(mergedConfiguration))
                .addCode(codeBlocks.entering(mergedConfiguration.getRepository(),
                        mergedConfiguration.getStreamEagerName()))
                .addCode(TypicalCodeBlocks.tryConnect())
                .addCode(codeBlocks.pickVendorQuery(vendorStatements))
                .addCode(TypicalCodeBlocks.tryPrepareStatement())
                .addCode(TypicalCodeBlocks.setParameters(mergedConfiguration))
                .addCode(codeBlocks.logExecutedQuery(mergedConfiguration))
                .addCode(TypicalCodeBlocks.tryExecute())
                .addCode(TypicalCodeBlocks.getMetaData())
                .addCode(TypicalCodeBlocks.getColumnCount())
                .addCode(codeBlocks.newResultState())
                .addCode(TypicalCodeBlocks.returnAsStream(listOfResults, converter.getAlias()))
                .addCode(TypicalCodeBlocks.endTryBlock(3))
                .addCode(TypicalCodeBlocks.maybeCatchAndRethrow(mergedConfiguration))
                .build();
    }

    @Override
    public MethodSpec streamLazyMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        final ResultRowConverter converter = mergedConfiguration.getResultConverter();
        final TypeName resultType = TypicalTypes.guessTypeName(converter.getResultType());
        final ParameterizedTypeName streamOfResults = TypicalTypes.streamOf(resultType);
        return TypicalMethods.publicMethod(mergedConfiguration.getStreamLazyName())
                .addAnnotations(annotations.generatedMethod(getClass()))
                .returns(streamOfResults)
                .addParameters(mergedConfiguration.getParameterSpecs())
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
        final TypeName resultType = TypicalTypes.guessTypeName(converter.getResultType());
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
