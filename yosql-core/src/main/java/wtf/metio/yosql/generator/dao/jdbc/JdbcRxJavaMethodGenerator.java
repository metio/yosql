/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao.jdbc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.xn__ho_hia.javapoet.TypeGuesser;
import io.reactivex.Emitter;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.api.RxJavaMethodGenerator;
import wtf.metio.yosql.generator.blocks.api.*;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcBlocks;
import wtf.metio.yosql.generator.helpers.TypicalTypes;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.sql.ResultRowConverter;
import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.model.sql.SqlStatement;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

import java.util.List;
import java.util.concurrent.Callable;

import static wtf.metio.yosql.generator.blocks.generic.CodeBlocks.code;

final class JdbcRxJavaMethodGenerator implements RxJavaMethodGenerator {

    private final RuntimeConfiguration runtime;
    private final ControlFlows controlFlows;
    private final Names names;
    private final AnnotationGenerator annotations;
    private final Javadoc javadoc;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbcBlocks;

    JdbcRxJavaMethodGenerator(
            final RuntimeConfiguration runtime,
            final ControlFlows controlFlows,
            final Names names,
            final AnnotationGenerator annotations,
            final Javadoc javadoc,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks) {
        this.runtime = runtime;
        this.controlFlows = controlFlows;
        this.names = names;
        this.annotations = annotations;
        this.logging = logging;
        this.javadoc = javadoc;
        this.jdbcBlocks = jdbcBlocks;
        this.methods = methods;
        this.parameters = parameters;
    }

    @Override
    public MethodSpec rxJava2ReadMethod(
            final SqlConfiguration mergedConfiguration,
            final List<SqlStatement> vendorStatements) {
        final var converter = mergedConfiguration.getResultRowConverter();
        final var resultType = TypeGuesser.guessTypeName(converter.getResultType());
        final var flowReturn = ParameterizedTypeName.get(TypicalTypes.FLOWABLE, resultType);

        final var initialState = createFlowState(mergedConfiguration, vendorStatements);
        final var generator = createFlowGenerator(converter);
        final var disposer = createFlowDisposer();

        return methods.publicMethod(mergedConfiguration.getFlowableName())
                .addJavadoc(javadoc.methodJavadoc(vendorStatements))
                .addAnnotations(annotations.generatedMethod())
                .returns(flowReturn)
                .addParameters(parameters.asParameterSpecs(mergedConfiguration.getParameters()))
                .addCode(logging.entering(mergedConfiguration.getRepository(),
                        mergedConfiguration.getFlowableName()))
                .addCode(jdbcBlocks.newFlowable(initialState, generator, disposer))
                .build();
    }

    private TypeSpec createFlowState(
            final SqlConfiguration sqlConfiguration,
            final List<SqlStatement> statements) {
        final var callable = ClassName.get(Callable.class);
        final var initialStateType = ParameterizedTypeName.get(callable,
                runtime.rxJava().flowStateClass());
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(initialStateType)
                .addMethod(methods.implementation("call")
                        .addAnnotations(annotations.generatedMethod())
                        .returns(runtime.rxJava().flowStateClass())
                        .addException(Exception.class)
                        .addCode(jdbcBlocks.connectionVariable())
                        .addCode(jdbcBlocks.pickVendorQuery(statements))
                        .addCode(jdbcBlocks.statementVariable())
                        .addCode(jdbcBlocks.setParameters(sqlConfiguration))
                        .addCode(jdbcBlocks.logExecutedQuery(sqlConfiguration))
                        .addCode(jdbcBlocks.resultSetVariable())
                        .addCode(jdbcBlocks.readMetaData())
                        .addCode(jdbcBlocks.reactColumnCount())
                        .addCode(jdbcBlocks.returnNewFlowState())
                        .build())
                .build();
    }

    private TypeSpec createFlowGenerator(final ResultRowConverter converter) {
        final var resultType = TypeGuesser.guessTypeName(converter.getResultType());
        final var biConsumer = ClassName.get(io.reactivex.functions.BiConsumer.class);
        final var rawEmitter = ClassName.get(Emitter.class);
        final var emitter = ParameterizedTypeName.get(rawEmitter, resultType);
        final var generatorType = ParameterizedTypeName.get(biConsumer,
                runtime.rxJava().flowStateClass(), emitter);
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(generatorType)
                .addMethod(methods.implementation("accept")
                        .addAnnotations(annotations.generatedMethod())
                        .addParameter(parameters.parameter(runtime.rxJava().flowStateClass(), names.state()))
                        .addParameter(parameters.parameter(emitter, names.emitter()))
                        .returns(void.class)
                        .addException(Exception.class)
                        .addCode(controlFlows.startTryBlock())
                        .addCode(controlFlows.ifHasNext())
                        .addStatement("$N.onNext($N.asUserType($N))", names.emitter(), converter.getAlias(),
                                names.state())
                        .addCode(controlFlows.nextElse())
                        .addStatement("$N.onComplete()", names.emitter())
                        .addCode(controlFlows.endIf())
                        .addCode(controlFlows.endTryBlock())
                        .addCode(controlFlows.catchAndDo(code("$N.onError($N)", names.emitter(), names.exception())))
                        .build())
                .build();
    }

    private TypeSpec createFlowDisposer() {
        final var consumerClass = ClassName.get(io.reactivex.functions.Consumer.class);
        final var disposerType = ParameterizedTypeName.get(consumerClass,
                runtime.rxJava().flowStateClass());
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(disposerType)
                .addMethod(methods.implementation("accept")
                        .addAnnotations(annotations.generatedMethod())
                        .addParameter(parameters.parameter(runtime.rxJava().flowStateClass(), names.state()))
                        .returns(void.class)
                        .addException(Exception.class)
                        .addCode(jdbcBlocks.closeState())
                        .build())
                .build();
    }

}
