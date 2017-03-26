/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.raw_jdbc;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import de.xn__ho_hia.javapoet.TypeGuesser;
import de.xn__ho_hia.yosql.generator.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.RxJavaMethodGenerator;
import de.xn__ho_hia.yosql.generator.helpers.TypicalCodeBlocks;
import de.xn__ho_hia.yosql.generator.helpers.TypicalMethods;
import de.xn__ho_hia.yosql.generator.helpers.TypicalNames;
import de.xn__ho_hia.yosql.generator.helpers.TypicalParameters;
import de.xn__ho_hia.yosql.generator.helpers.TypicalTypes;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;
import io.reactivex.Emitter;

@SuppressWarnings({ "nls", "javadoc" })
public class RawJdbcRxJavaMethodGenerator implements RxJavaMethodGenerator {

    private final ExecutionConfiguration configuration;
    private final TypicalCodeBlocks      codeBlocks;
    private final AnnotationGenerator    annotations;

    @Inject
    public RawJdbcRxJavaMethodGenerator(
            final ExecutionConfiguration configuration,
            final TypicalCodeBlocks codeBlocks,
            final AnnotationGenerator annotations) {
        this.configuration = configuration;
        this.codeBlocks = codeBlocks;
        this.annotations = annotations;
    }

    @Override
    public MethodSpec rxJava2ReadMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        final ResultRowConverter converter = mergedConfiguration.getResultRowConverter();
        final TypeName resultType = TypeGuesser.guessTypeName(converter.getResultType());
        final ParameterizedTypeName flowReturn = ParameterizedTypeName.get(TypicalTypes.FLOWABLE, resultType);

        final TypeSpec initialState = createFlowState(mergedConfiguration, vendorStatements);
        final TypeSpec generator = createFlowGenerator(converter);
        final TypeSpec disposer = createFlowDisposer();

        return TypicalMethods.publicMethod(mergedConfiguration.getFlowableName())
                .addAnnotations(annotations.generatedMethod(getClass()))
                .returns(flowReturn)
                .addParameters(TypicalParameters.asParameterSpecs(mergedConfiguration.getParameters()))
                .addCode(codeBlocks.entering(mergedConfiguration.getRepository(),
                        mergedConfiguration.getFlowableName()))
                .addCode(TypicalCodeBlocks.newFlowable(initialState, generator, disposer))
                .build();
    }

    private TypeSpec createFlowState(
            final SqlConfiguration sqlConfiguration,
            final List<SqlStatement> statements) {
        final ClassName callable = ClassName.get(Callable.class);
        final ParameterizedTypeName initialStateType = ParameterizedTypeName.get(callable,
                configuration.flowStateClass());
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(initialStateType)
                .addMethod(TypicalMethods.implementation("call")
                        .addAnnotations(annotations.generatedMethod(getClass()))
                        .returns(configuration.flowStateClass())
                        .addException(Exception.class)
                        .addCode(TypicalCodeBlocks.getConnection())
                        .addCode(codeBlocks.pickVendorQuery(statements))
                        .addCode(TypicalCodeBlocks.prepareStatement())
                        .addCode(TypicalCodeBlocks.setParameters(sqlConfiguration))
                        .addCode(codeBlocks.logExecutedQuery(sqlConfiguration))
                        .addCode(TypicalCodeBlocks.executeQuery())
                        .addCode(TypicalCodeBlocks.getMetaData())
                        .addCode(TypicalCodeBlocks.getColumnCount())
                        .addCode(codeBlocks.returnNewFlowState())
                        .build())
                .build();
    }

    private TypeSpec createFlowGenerator(final ResultRowConverter converter) {
        final TypeName resultType = TypeGuesser.guessTypeName(converter.getResultType());
        final ClassName biConsumer = ClassName.get(io.reactivex.functions.BiConsumer.class);
        final ClassName rawEmitter = ClassName.get(Emitter.class);
        final ParameterizedTypeName emitter = ParameterizedTypeName.get(rawEmitter, resultType);
        final ParameterizedTypeName generatorType = ParameterizedTypeName.get(biConsumer,
                configuration.flowStateClass(), emitter);
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(generatorType)
                .addMethod(TypicalMethods.implementation("accept")
                        .addAnnotations(annotations.generatedMethod(getClass()))
                        .addParameter(
                                TypicalParameters.parameter(configuration.flowStateClass(), TypicalNames.STATE))
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
                configuration.flowStateClass());
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(disposerType)
                .addMethod(TypicalMethods.implementation("accept")
                        .addAnnotations(annotations.generatedMethod(getClass()))
                        .addParameter(TypicalParameters.parameter(configuration.flowStateClass(),
                                TypicalNames.STATE))
                        .returns(void.class)
                        .addException(Exception.class)
                        .addCode(TypicalCodeBlocks.closeState())
                        .build())
                .build();
    }

}
