package com.github.sebhoss.yosql.generator.jdbc;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.helpers.TypicalCodeBlocks;
import com.github.sebhoss.yosql.helpers.TypicalMethods;
import com.github.sebhoss.yosql.helpers.TypicalNames;
import com.github.sebhoss.yosql.helpers.TypicalParameters;
import com.github.sebhoss.yosql.helpers.TypicalTypes;
import com.github.sebhoss.yosql.model.ResultRowConverter;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.model.SqlStatementConfiguration;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

@Named
@Singleton
public class Java8MethodAPI {

    private final TypicalCodeBlocks codeBlocks;

    @Inject
    public Java8MethodAPI(final TypicalCodeBlocks codeBlocks) {
        this.codeBlocks = codeBlocks;
    }

    public MethodSpec streamEagerApi(final List<SqlStatement> statements) {
        final SqlStatementConfiguration configuration = SqlStatementConfiguration.merge(statements);
        final ResultRowConverter converter = configuration.getResultConverter();
        final ClassName resultType = ClassName.bestGuess(converter.getResultType());
        final ParameterizedTypeName listOfResults = ParameterizedTypeName.get(TypicalTypes.LIST, resultType);
        final ParameterizedTypeName streamOfResults = ParameterizedTypeName.get(TypicalTypes.STREAM, resultType);
        return TypicalMethods.publicMethod(configuration.getStreamEagerName())
                .returns(streamOfResults)
                .addParameters(configuration.getParameterSpecs())
                .addExceptions(TypicalCodeBlocks.sqlException(configuration))
                .addCode(TypicalCodeBlocks.tryConnect())
                .addCode(TypicalCodeBlocks.pickVendorQuery(statements))
                .addCode(TypicalCodeBlocks.tryPrepare())
                .addCode(TypicalCodeBlocks.setParameters(configuration))
                .addCode(TypicalCodeBlocks.tryExecute())
                .addCode(TypicalCodeBlocks.getMetaData())
                .addCode(TypicalCodeBlocks.getColumnCount())
                .addCode(codeBlocks.newResultState())
                .addCode(TypicalCodeBlocks.returnAsStream(listOfResults, converter.getAlias()))
                .addCode(TypicalCodeBlocks.endTryBlock(3))
                .addCode(TypicalCodeBlocks.maybeCatchAndRethrow(configuration))
                .build();
    }

    public MethodSpec streamLazyApi(final List<SqlStatement> statements) {
        final SqlStatementConfiguration configuration = SqlStatementConfiguration.merge(statements);
        final ResultRowConverter converter = configuration.getResultConverter();
        final ClassName resultType = ClassName.bestGuess(converter.getResultType());
        final ParameterizedTypeName streamOfResults = ParameterizedTypeName.get(TypicalTypes.STREAM, resultType);
        return TypicalMethods.publicMethod(configuration.getStreamLazyName())
                .returns(streamOfResults)
                .addParameters(configuration.getParameterSpecs())
                .addExceptions(TypicalCodeBlocks.sqlException(configuration))
                .addCode(TypicalCodeBlocks.maybeTry(configuration))
                .addCode(TypicalCodeBlocks.getConnection())
                .addCode(TypicalCodeBlocks.pickVendorQuery(statements))
                .addCode(TypicalCodeBlocks.prepareStatement())
                .addCode(TypicalCodeBlocks.setParameters(configuration))
                .addCode(TypicalCodeBlocks.executeQuery())
                .addCode(TypicalCodeBlocks.getMetaData())
                .addCode(TypicalCodeBlocks.getColumnCount())
                .addCode(codeBlocks.newResultState())
                .addCode(TypicalCodeBlocks.streamStatefull(lazyStreamSpliterator(converter), lazyStreamCloser()))
                .addCode(TypicalCodeBlocks.endMaybeTry(configuration))
                .addCode(TypicalCodeBlocks.maybeCatchAndRethrow(configuration))
                .build();
    }

    private TypeSpec lazyStreamSpliterator(final ResultRowConverter converter) {
        final ClassName spliteratorClass = ClassName.get(Spliterators.AbstractSpliterator.class);
        final ClassName resultType = ClassName.bestGuess(converter.getResultType());
        final ParameterizedTypeName superinterface = ParameterizedTypeName.get(spliteratorClass, resultType);
        final ParameterizedTypeName consumerType = ParameterizedTypeName.get(TypicalTypes.CONSUMER,
                WildcardTypeName.supertypeOf(resultType));
        return TypeSpec
                .anonymousClassBuilder("$T.MAX_VALUE, $T.ORDERED", Long.class, Spliterator.class)
                .addSuperinterface(superinterface)
                .addMethod(TypicalMethods.implementation("tryAdvance")
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
