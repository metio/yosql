package com.github.sebhoss.yosql.generator.raw_jdbc;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.generator.AnnotationGenerator;
import com.github.sebhoss.yosql.generator.RxJavaMethodGenerator;
import com.github.sebhoss.yosql.generator.helpers.TypicalCodeBlocks;
import com.github.sebhoss.yosql.generator.helpers.TypicalMethods;
import com.github.sebhoss.yosql.generator.helpers.TypicalNames;
import com.github.sebhoss.yosql.generator.helpers.TypicalParameters;
import com.github.sebhoss.yosql.generator.helpers.TypicalTypes;
import com.github.sebhoss.yosql.model.ResultRowConverter;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.model.SqlStatementConfiguration;
import com.github.sebhoss.yosql.plugin.PluginConfig;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import io.reactivex.Emitter;

@Named
@Singleton
public class RawJdbcRxJavaMethodGenerator implements RxJavaMethodGenerator {

    private final PluginConfig        pluginConfig;
    private final TypicalCodeBlocks   codeBlocks;
    private final AnnotationGenerator annotations;

    @Inject
    public RawJdbcRxJavaMethodGenerator(
            final PluginConfig pluginConfig,
            final TypicalCodeBlocks codeBlocks,
            final AnnotationGenerator annotations) {
        this.pluginConfig = pluginConfig;
        this.codeBlocks = codeBlocks;
        this.annotations = annotations;
    }

    @Override
    public MethodSpec rxJava2ReadMethod(final List<SqlStatement> statements) {
        final SqlStatementConfiguration configuration = SqlStatementConfiguration.merge(statements);
        final ResultRowConverter converter = configuration.getResultConverter();
        final ClassName resultType = ClassName.bestGuess(converter.getResultType());
        final ParameterizedTypeName flowReturn = ParameterizedTypeName.get(TypicalTypes.FLOWABLE, resultType);

        final TypeSpec initialState = createFlowState(configuration, statements);
        final TypeSpec generator = createFlowGenerator(converter);
        final TypeSpec disposer = createFlowDisposer();

        return TypicalMethods.publicMethod(configuration.getFlowableName())
                .addAnnotations(annotations.generatedMethod(getClass()))
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
                pluginConfig.getFlowStateClass());
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(initialStateType)
                .addMethod(TypicalMethods.implementation("call")
                        .addAnnotations(annotations.generatedMethod(getClass()))
                        .returns(pluginConfig.getFlowStateClass())
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
                pluginConfig.getFlowStateClass(), emitter);
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(generatorType)
                .addMethod(TypicalMethods.implementation("accept")
                        .addAnnotations(annotations.generatedMethod(getClass()))
                        .addParameter(
                                TypicalParameters.parameter(pluginConfig.getFlowStateClass(), TypicalNames.STATE))
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
                pluginConfig.getFlowStateClass());
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(disposerType)
                .addMethod(TypicalMethods.implementation("accept")
                        .addAnnotations(annotations.generatedMethod(getClass()))
                        .addParameter(TypicalParameters.parameter(pluginConfig.getFlowStateClass(),
                                TypicalNames.STATE))
                        .returns(void.class)
                        .addException(Exception.class)
                        .addCode(TypicalCodeBlocks.closeState())
                        .build())
                .build();
    }

}
