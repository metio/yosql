/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import de.xn__ho_hia.javapoet.TypeGuesser;
import io.reactivex.Emitter;
import wtf.metio.yosql.codegen.api.ControlFlows;
import wtf.metio.yosql.codegen.api.Methods;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.codegen.api.RxJavaMethodGenerator;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.util.List;
import java.util.concurrent.Callable;

import static wtf.metio.yosql.codegen.blocks.CodeBlocks.code;

public final class JdbcRxJavaMethodGenerator implements RxJavaMethodGenerator {

    private final JdbcConfiguration config;
    private final ControlFlows controlFlows;
    private final NamesConfiguration names;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;

    public JdbcRxJavaMethodGenerator(
            final JdbcConfiguration config,
            final ControlFlows controlFlows,
            final NamesConfiguration names,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc) {
        this.config = config;
        this.controlFlows = controlFlows;
        this.names = names;
        this.logging = logging;
        this.jdbc = jdbc;
        this.methods = methods;
        this.parameters = parameters;
    }

    @Override
    public MethodSpec rxJava2ReadMethod(
            final SqlConfiguration configuration,
            final List<SqlStatement> statements) {
        final var converter = configuration.resultRowConverter().orElse(config.defaultConverter().orElseThrow());
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var flowReturn = ParameterizedTypeName.get(TypicalTypes.FLOWABLE, resultType);

        final var initialState = createFlowState(configuration, statements);
        final var generator = createFlowGenerator(converter);
        final var disposer = createFlowDisposer();

        return methods.rxJavaMethod(configuration.flowableName(), statements)
                .returns(flowReturn)
                .addParameters(parameters.asParameterSpecs(configuration.parameters()))
                .addCode(logging.entering(configuration.repository(),
                        configuration.flowableName()))
                .addCode(jdbc.newFlowable(initialState, generator, disposer))
                .build();
    }

    private TypeSpec createFlowState(
            final SqlConfiguration sqlConfiguration,
            final List<SqlStatement> statements) {
        final var callable = ClassName.get(Callable.class);
        final var initialStateType = ParameterizedTypeName.get(callable,
                config.flowStateClass());
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(initialStateType)
                .addMethod(methods.implementation("call")
                        .returns(config.flowStateClass())
                        .addException(Exception.class)
                        .addStatement(jdbc.connectionVariable())
                        .addCode(jdbc.pickVendorQuery(statements))
                        .addStatement(jdbc.statementVariable())
                        .addCode(jdbc.setParameters(sqlConfiguration))
                        .addCode(jdbc.logExecutedQuery(sqlConfiguration))
                        .addCode(jdbc.resultSetVariableStatement())
                        .addCode(jdbc.readMetaData())
                        .addCode(jdbc.readColumnCount())
                        .addCode(jdbc.returnNewFlowState())
                        .build())
                .build();
    }

    private TypeSpec createFlowGenerator(final ResultRowConverter converter) {
        final var resultType = TypeGuesser.guessTypeName(converter.resultType());
        final var biConsumer = ClassName.get(io.reactivex.functions.BiConsumer.class);
        final var rawEmitter = ClassName.get(Emitter.class);
        final var emitter = ParameterizedTypeName.get(rawEmitter, resultType);
        final var generatorType = ParameterizedTypeName.get(biConsumer,
                config.flowStateClass(), emitter);
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(generatorType)
                .addMethod(methods.implementation("accept")
                        .addParameter(parameters.parameter(config.flowStateClass(), names.state()))
                        .addParameter(parameters.parameter(emitter, names.emitter()))
                        .returns(void.class)
                        .addException(Exception.class)
                        .addCode(controlFlows.startTryBlock())
                        .addCode(controlFlows.ifHasNext())
                        .addStatement("$N.onNext($N.$N($N))", names.emitter(), converter.alias(),
                                converter.methodName(), names.state())
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
                config.flowStateClass());
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(disposerType)
                .addMethod(methods.implementation("accept")
                        .addParameter(parameters.parameter(config.flowStateClass(), names.state()))
                        .returns(void.class)
                        .addException(Exception.class)
                        .addCode(jdbc.closeState())
                        .build())
                .build();
    }

}
