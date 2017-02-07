package com.github.sebhoss.yosql.generator;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.PluginRuntimeConfig;
import com.github.sebhoss.yosql.ResultRowConverter;
import com.github.sebhoss.yosql.SqlStatement;
import com.github.sebhoss.yosql.SqlStatementConfiguration;
import com.github.sebhoss.yosql.helpers.TypicalCodeBlocks;
import com.github.sebhoss.yosql.helpers.TypicalMethods;
import com.github.sebhoss.yosql.helpers.TypicalNames;
import com.github.sebhoss.yosql.helpers.TypicalParameters;
import com.github.sebhoss.yosql.helpers.TypicalTypes;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import io.reactivex.Emitter;

@Named
@Singleton
public class RxJava2MethodAPI {

    private final PluginRuntimeConfig runtimeConfig;
    private final TypicalCodeBlocks   codeBlocks;

    @Inject
    public RxJava2MethodAPI(
            final PluginRuntimeConfig runtimeConfig,
            final TypicalCodeBlocks codeBlocks) {
        this.runtimeConfig = runtimeConfig;
        this.codeBlocks = codeBlocks;
    }

    public MethodSpec rxJava2Method(final List<SqlStatement> statements) {
        final SqlStatementConfiguration configuration = SqlStatementConfiguration.merge(statements);
        final ResultRowConverter converter = configuration.getResultConverter();
        final ClassName resultType = ClassName.bestGuess(converter.getResultType());
        final ParameterizedTypeName flowReturn = ParameterizedTypeName.get(TypicalTypes.FLOWABLE, resultType);

        final TypeSpec initialState = createFlowState(configuration, statements);
        final TypeSpec generator = createFlowGenerator(converter);
        final TypeSpec disposer = createFlowDisposer();

        return TypicalMethods.publicMethod(configuration.getFlowableName())
                .returns(flowReturn)
                .addParameters(configuration.getParameterSpecs())
                .addCode(TypicalCodeBlocks.newFlowable(initialState, generator, disposer))
                .build();
    }

    private TypeSpec createFlowState(
            final SqlStatementConfiguration configuration,
            final List<SqlStatement> statements) {
        final ClassName callable = ClassName.get(Callable.class);
        final ParameterizedTypeName initialStateType = ParameterizedTypeName.get(callable,
                runtimeConfig.getFlowStateClass());
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(initialStateType)
                .addMethod(TypicalMethods.implementation("call")
                        .returns(runtimeConfig.getFlowStateClass())
                        .addException(Exception.class)
                        .addCode(TypicalCodeBlocks.getConnection())
                        .addCode(TypicalCodeBlocks.pickVendorQuery(statements))
                        .addCode(TypicalCodeBlocks.prepareStatement())
                        .addCode(TypicalCodeBlocks.setParameters(configuration))
                        .addCode(TypicalCodeBlocks.executeQuery())
                        .addCode(TypicalCodeBlocks.getMetaData())
                        .addCode(TypicalCodeBlocks.getColumnCount())
                        .addCode(codeBlocks.newFlowState())
                        .build())
                .build();
    }

    private TypeSpec createFlowGenerator(final ResultRowConverter converter) {
        final ClassName resultType = ClassName.bestGuess(converter.getResultType());
        final ClassName biConsumer = ClassName.get(io.reactivex.functions.BiConsumer.class);
        final ClassName rawEmitter = ClassName.get(Emitter.class);
        final ParameterizedTypeName emitter = ParameterizedTypeName.get(rawEmitter, resultType);
        final ParameterizedTypeName generatorType = ParameterizedTypeName.get(biConsumer,
                runtimeConfig.getFlowStateClass(), emitter);
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(generatorType)
                .addMethod(TypicalMethods.implementation("accept")
                        .addParameter(
                                TypicalParameters.parameter(runtimeConfig.getFlowStateClass(), TypicalNames.STATE))
                        .addParameter(TypicalParameters.parameter(emitter, TypicalNames.EMITTER))
                        .returns(void.class)
                        .addException(Exception.class)
                        .addCode(TypicalCodeBlocks.startTryBlock())
                        .addCode(TypicalCodeBlocks.ifHasNext())
                        .addStatement("$N.onNext($N.asUserType($N))", TypicalNames.EMITTER, converter.getAlias(),
                                TypicalNames.STATE)
                        .addCode(TypicalCodeBlocks.nextElse())
                        .addStatement("$N.onComplete()", TypicalNames.EMITTER)
                        .addCode(TypicalCodeBlocks.endIf())
                        .addCode(TypicalCodeBlocks.endTryBlock())
                        .addCode(TypicalCodeBlocks.catchAndDo("$N.onError($N)", TypicalNames.EMITTER,
                                TypicalNames.EXCEPTION))
                        .build())
                .build();
    }

    private TypeSpec createFlowDisposer() {
        final ClassName consumerClass = ClassName.get(io.reactivex.functions.Consumer.class);
        final ParameterizedTypeName disposerType = ParameterizedTypeName.get(consumerClass,
                runtimeConfig.getFlowStateClass());
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(disposerType)
                .addMethod(TypicalMethods.implementation("accept")
                        .addParameter(TypicalParameters.parameter(runtimeConfig.getFlowStateClass(),
                                TypicalNames.STATE))
                        .returns(void.class)
                        .addException(Exception.class)
                        .addCode(TypicalCodeBlocks.closeState())
                        .build())
                .build();
    }

}
