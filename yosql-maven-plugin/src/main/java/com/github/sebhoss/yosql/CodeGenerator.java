package com.github.sebhoss.yosql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.Generated;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;

import com.github.sebhoss.yosql.generator.FlowStateGenerator;
import com.github.sebhoss.yosql.generator.ResultStateGenerator;
import com.github.sebhoss.yosql.generator.TypicalCodeBlocks;
import com.github.sebhoss.yosql.generator.TypicalMethods;
import com.github.sebhoss.yosql.generator.TypicalModifiers;
import com.github.sebhoss.yosql.generator.TypicalNames;
import com.github.sebhoss.yosql.generator.TypicalParameters;
import com.github.sebhoss.yosql.generator.TypicalTypes;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import io.reactivex.Emitter;
import io.reactivex.Flowable;

@Named
@Singleton
public class CodeGenerator {

    private final PluginErrors         pluginErrors;
    private final FlowStateGenerator   flowStateGenerator;
    private final PluginRuntimeConfig  runtimeConfig;
    private final ResultStateGenerator resultStateGenerator;

    private ClassName                  flowState;
    private ClassName                  resultState;

    @Inject
    public CodeGenerator(
            final FlowStateGenerator flowStateGenerator,
            final ResultStateGenerator resultStateGenerator,
            final PluginErrors pluginErrors,
            final PluginRuntimeConfig runtimeConfig) {
        this.flowStateGenerator = flowStateGenerator;
        this.resultStateGenerator = resultStateGenerator;
        this.pluginErrors = pluginErrors;
        this.runtimeConfig = runtimeConfig;
    }

    public void generateUtilities(final List<SqlStatement> allStatements) {
        if (allStatements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlStatementType.READING == config.getType())
                .anyMatch(config -> config.isGenerateRxJavaApi())) {
            resultStateGenerator.generateResultStateClass();
            flowStateGenerator.generateFlowStateClass();
        }
    }

    /**
     * Generates a single repository.
     *
     * @param repositoryName
     *            The fully-qualified name of the repository to generate.
     * @param sqlStatements
     *            The SQL statements to be included in the repository.
     */
    public void generateRepository(final String repositoryName,
            final List<SqlStatement> sqlStatements) {
        flowState = ClassName.get(runtimeConfig.getBasePackageName() + "." + runtimeConfig.getUtilityPackageName(),
                FlowStateGenerator.FLOW_STATE_CLASS_NAME);
        resultState = ClassName.get(runtimeConfig.getBasePackageName() + "." + runtimeConfig.getUtilityPackageName(),
                ResultStateGenerator.RESULT_STATE_CLASS_NAME);

        final String className = getClassName(repositoryName);
        final String packageName = getPackageName(repositoryName);
        final TypeSpec repository = TypeSpec.classBuilder(className)
                .addModifiers(TypicalModifiers.PUBLIC_CLASS)
                .addFields(asFields(sqlStatements))
                .addMethods(asMethods(sqlStatements))
                .addAnnotation(generatedAnnotation())
                .addStaticBlock(staticInitializer(sqlStatements))
                .build();
        final JavaFile javaFile = JavaFile.builder(packageName, repository).build();

        try {
            javaFile.writeTo(runtimeConfig.getOutputBaseDirectory().toPath());
            runtimeConfig.getLogger().info(String.format("Generated [%s.%s] using %s statements", packageName,
                    className, sqlStatements.size()));
        } catch (final IOException exception) {
            pluginErrors.add(exception);
        }
    }

    private static CodeBlock staticInitializer(final List<SqlStatement> sqlStatements) {
        final CodeBlock.Builder builder = CodeBlock.builder();
        sqlStatements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(SqlStatementConfiguration::hasParameters)
                .forEach(config -> {
                    config.getParameters().stream()
                            .filter(SqlParameter::hasIndices)
                            .forEach(param -> builder.addStatement("$N.put($S, $L)",
                                    constsantSqlStatementParametersField(config), param.getName(),
                                    indexArray(param)));
                });
        return builder.build();
    }

    private static String indexArray(final SqlParameter param) {
        return IntStream.of(param.getIndices())
                .boxed()
                .map(Object::toString)
                .collect(Collectors.joining(", ", "new int[] { ", " }"));
    }

    private static AnnotationSpec generatedAnnotation() {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", CodeGenerator.class.getName())
                .addMember("date", "$S", ZonedDateTime.now().toString())
                .addMember("comments", "$S", "DO NOT EDIT")
                .build();
    }

    private String getPackageName(final String repositoryName) {
        return repositoryName.substring(0, repositoryName.lastIndexOf("."));
    }

    private String getClassName(final String repositoryName) {
        return repositoryName.substring(repositoryName.lastIndexOf(".") + 1,
                repositoryName.length());
    }

    private Iterable<FieldSpec> asFields(final List<SqlStatement> sqlStatements) {
        final Stream<FieldSpec> constants = Stream.concat(
                sqlStatements.stream()
                        .map(CodeGenerator::asConstantSqlField),
                sqlStatements.stream()
                        .filter(stmt -> !stmt.getConfiguration().getParameters().isEmpty())
                        .map(CodeGenerator::asConstantSqlNamedParamsField));
        final Stream<FieldSpec> fields = Stream.of(asDataSourceField());
        return Stream.concat(constants, fields)
                .collect(Collectors.toList());
    }

    private static FieldSpec asConstantSqlField(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        return FieldSpec.builder(String.class, constantSqlStatementField(configuration))
                .addModifiers(TypicalModifiers.CONSTANT_FIELD)
                .initializer("$S", sqlStatement.getStatement().replaceAll(SqlFileParser.PATTERN.pattern(), "?"))
                .build();
    }

    private static FieldSpec asConstantSqlNamedParamsField(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        return FieldSpec.builder(TypicalTypes.MAP_OF_STRING_AND_NUMBERS,
                constsantSqlStatementParametersField(configuration))
                .addModifiers(TypicalModifiers.CONSTANT_FIELD)
                .initializer("new $T<>($L)", HashMap.class, sqlStatement.getConfiguration().getParameters().size())
                .build();
    }

    private static String constsantSqlStatementParametersField(final SqlStatementConfiguration configuration) {
        return constantSqlStatementField(configuration) + "_PARAMETER_INDEX";
    }

    private static FieldSpec asDataSourceField() {
        return FieldSpec.builder(DataSource.class, TypicalNames.DATA_SOURCE)
                .addModifiers(TypicalModifiers.PRIVATE_FIELD)
                .build();
    }

    private Iterable<MethodSpec> asMethods(final List<SqlStatement> sqlStatements) {
        final Stream<MethodSpec> constructors = Stream.of(constructor());
        final Stream<MethodSpec> singleQueries = sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isGenerateStandardApi())
                .map(this::singleQuery);
        final Stream<MethodSpec> batchQueries = sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isGenerateBatchApi())
                .filter(statement -> statement.getConfiguration().hasParameters())
                .filter(statement -> SqlStatementType.WRITING == statement.getConfiguration().getType())
                .map(CodeGenerator::batchQuery);
        final Stream<MethodSpec> streamQueries = Stream.concat(sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isGenerateStreamEagerApi())
                .filter(statement -> SqlStatementType.READING == statement.getConfiguration().getType())
                .map(this::streamQueryEager), sqlStatements.stream()
                        .filter(statement -> statement.getConfiguration().isGenerateStreamLazyApi())
                        .filter(statement -> SqlStatementType.READING == statement.getConfiguration().getType())
                        .map(this::streamQueryLazy));
        final Stream<MethodSpec> rxJavaQueries = sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isGenerateRxJavaApi())
                .filter(statement -> SqlStatementType.READING == statement.getConfiguration().getType())
                .map(this::flowWithBackpressure);

        sqlStatements.stream()
                .anyMatch(this::shouldBuildResultToMapHelperMethod);

        final Stream<MethodSpec> utilities = Stream.concat(
                sqlStatements.stream()
                        .filter(statement -> shouldBuildResultToListHelperMethod(statement))
                        .map(statement -> resultSetToList())
                        .limit(1),
                sqlStatements.stream()
                        .filter(statement -> shouldBuildResultToMapHelperMethod(statement))
                        .map(statement -> resultSetToMap())
                        .limit(1));
        return Stream.concat(constructors,
                Stream.concat(singleQueries,
                        Stream.concat(batchQueries,
                                Stream.concat(streamQueries,
                                        Stream.concat(rxJavaQueries, utilities)))))
                .collect(Collectors.toList());
    }

    private static MethodSpec constructor() {
        return TypicalMethods.constructor()
                .addParameter(TypicalParameters.dataSource())
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.DATA_SOURCE))
                .build();
    }

    private boolean shouldBuildResultToMapHelperMethod(final SqlStatement statement) {
        return statement.getConfiguration().isGenerateStreamLazyApi()
                || shouldBuildResultToListHelperMethod(statement);
    }

    private boolean shouldBuildResultToListHelperMethod(final SqlStatement statement) {
        return statement.getConfiguration().isGenerateStreamEagerApi()
                || statement.getConfiguration().isGenerateStandardApi()
                        && SqlStatementType.READING == statement.getConfiguration().getType();
    }

    private MethodSpec singleQuery(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        if (SqlStatementType.READING == configuration.getType()) {
            return TypicalMethods.publicMethod(configuration.getName())
                    .returns(TypicalTypes.LIST_OF_MAPS)
                    .addParameters(configuration.getParameterSpecs())
                    .addCode(tryAllPreparedAutoCloseableResources(configuration))
                    .addCode(setParameters(configuration))
                    .beginControlFlow("try (final $T $N = $N.executeQuery())", ResultSet.class,
                            TypicalNames.RESULT_SET, TypicalNames.PREPARED_STATEMENT)
                    .addCode(TypicalCodeBlocks.getMetaData())
                    .addCode(TypicalCodeBlocks.getColumnCount())
                    .addStatement("final $T $N = new $T($N, $N, $N)", resultState, "result", resultState,
                            TypicalNames.RESULT_SET, TypicalNames.META_DATA, TypicalNames.COLUMN_COUNT)
                    .addStatement("return resultSetToList($N)", "result")
                    .endControlFlow()
                    .endControlFlow()
                    .addCode(catchAndRethrow())
                    .build();
        } else {
            return TypicalMethods.publicMethod(configuration.getName())
                    .returns(int.class)
                    .addParameters(configuration.getParameterSpecs())
                    .addCode(tryAllPreparedAutoCloseableResources(configuration))
                    .addCode(setParameters(configuration))
                    .addStatement("return $N.executeUpdate()", TypicalNames.PREPARED_STATEMENT)
                    .endControlFlow()
                    .addCode(catchAndRethrow())
                    .build();
        }
    }

    private MethodSpec flowWithBackpressure(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();

        final TypeSpec initialState = createFlowState(configuration, flowState);
        final TypeSpec generator = createFlowGenerator(flowState);
        final TypeSpec disposer = createFlowDisposer(flowState);

        return TypicalMethods.publicMethod(configuration.getFlowableName())
                .returns(TypicalTypes.FLOWABLE_OF_MAPS)
                .addParameters(configuration.getParameterSpecs())
                .addStatement("return $T.generate($L, $L, $L)", Flowable.class, initialState, generator, disposer)
                .build();
    }

    private TypeSpec createFlowState(final SqlStatementConfiguration configuration, final ClassName flowState) {
        final ClassName callable = ClassName.get(Callable.class);
        final ParameterizedTypeName initialStateType = ParameterizedTypeName.get(callable, flowState);
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(initialStateType)
                .addMethod(TypicalMethods.implementation("call")
                        .returns(flowState)
                        .addException(Exception.class)
                        .addStatement("final $T $N = $N.getConnection()", Connection.class, TypicalNames.CONNECTION,
                                TypicalNames.DATA_SOURCE)
                        .addStatement("final $T $N = $N.prepareStatement($N)", PreparedStatement.class,
                                TypicalNames.PREPARED_STATEMENT, TypicalNames.CONNECTION,
                                constantSqlStatementField(configuration))
                        .addCode(setParameters(configuration))
                        .addCode(TypicalCodeBlocks.executeQuery())
                        .addCode(TypicalCodeBlocks.getMetaData())
                        .addCode(TypicalCodeBlocks.getColumnCount())
                        .addStatement("return new $T($N, $N, $N, $N, $N)", flowState, TypicalNames.CONNECTION,
                                TypicalNames.PREPARED_STATEMENT, TypicalNames.RESULT_SET, TypicalNames.META_DATA,
                                TypicalNames.COLUMN_COUNT)
                        .build())
                .build();
    }

    private TypeSpec createFlowGenerator(final ClassName flowState) {
        final ClassName biConsumer = ClassName.get(io.reactivex.functions.BiConsumer.class);
        final ClassName rawEmitter = ClassName.get(Emitter.class);
        final ParameterizedTypeName emitter = ParameterizedTypeName.get(rawEmitter,
                TypicalTypes.MAP_OF_STRING_AND_OBJECTS);
        final ParameterizedTypeName generatorType = ParameterizedTypeName.get(biConsumer, flowState, emitter);
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(generatorType)
                .addMethod(TypicalMethods.implementation("accept")
                        .addParameter(flowState, TypicalNames.STATE, TypicalModifiers.PARAMETER)
                        .addParameter(emitter, TypicalNames.EMITTER, TypicalModifiers.PARAMETER)
                        .returns(void.class)
                        .addException(Exception.class)
                        .beginControlFlow("try")
                        .beginControlFlow("if ($N.next())", TypicalNames.STATE)
                        .addStatement("$N.onNext(resultSetToMap($N))", TypicalNames.EMITTER,
                                TypicalNames.STATE)
                        .nextControlFlow("else")
                        .addStatement("$N.onComplete()", TypicalNames.EMITTER)
                        .endControlFlow()
                        .endControlFlow()
                        .beginControlFlow("catch (final $T $N)", SQLException.class, TypicalNames.EXCEPTION)
                        .addStatement("$N.onError($N)", TypicalNames.EMITTER, TypicalNames.EXCEPTION)
                        .endControlFlow()
                        .build())
                .build();
    }

    private TypeSpec createFlowDisposer(final ClassName flowState) {
        final ClassName consumerClass = ClassName.get(io.reactivex.functions.Consumer.class);
        final ParameterizedTypeName disposerType = ParameterizedTypeName.get(consumerClass, flowState);
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(disposerType)
                .addMethod(TypicalMethods.implementation("accept")
                        .addParameter(TypicalParameters.parameter(flowState, TypicalNames.STATE))
                        .returns(void.class)
                        .addException(Exception.class)
                        .addStatement("$N.close()", TypicalNames.STATE)
                        .build())
                .build();
    }

    private static MethodSpec batchQuery(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        return MethodSpec.methodBuilder(configuration.getBatchName())
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .returns(TypicalTypes.ARRAY_OF_INTS)
                .addParameters(configuration.getBatchParameterSpecs())
                .addCode(tryAllPreparedAutoCloseableResources(configuration))
                .beginControlFlow("for (int $N = 0; $N < $N.length; $N++)", TypicalNames.BATCH, TypicalNames.BATCH,
                        configuration.getParameters().get(0).getName(), TypicalNames.BATCH)
                .addCode(setBatchParameters(configuration))
                .addStatement("$N.addBatch()", TypicalNames.PREPARED_STATEMENT)
                .endControlFlow()
                .addStatement("return $N.executeBatch()", TypicalNames.PREPARED_STATEMENT)
                .endControlFlow()
                .addCode(catchAndRethrow())
                .build();
    }

    private MethodSpec streamQueryEager(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        return MethodSpec.methodBuilder(configuration.getStreamEagerName())
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .returns(TypicalTypes.STREAM_OF_MAPS)
                .addParameters(configuration.getParameterSpecs())
                .addCode(tryAllPreparedAutoCloseableResources(configuration))
                .addCode(setParameters(configuration))
                .beginControlFlow("try (final $T $N = $N.executeQuery())", ResultSet.class, TypicalNames.RESULT_SET,
                        TypicalNames.PREPARED_STATEMENT)
                .addCode(TypicalCodeBlocks.getMetaData())
                .addCode(TypicalCodeBlocks.getColumnCount())
                .addStatement("final $T $N = new $T($N, $N, $N)", resultState, "result", resultState,
                        TypicalNames.RESULT_SET, TypicalNames.META_DATA, TypicalNames.COLUMN_COUNT)
                .addStatement("final $T resultList = resultSetToList($N)", TypicalTypes.LIST_OF_MAPS,
                        "result")
                .addStatement("return $N.stream()", "resultList")
                .endControlFlow()
                .endControlFlow()
                .addCode(catchAndRethrow()).build();
    }

    private MethodSpec streamQueryLazy(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        final ClassName spliteratorClass = ClassName.get(Spliterators.AbstractSpliterator.class);
        final ParameterizedTypeName superinterface = ParameterizedTypeName.get(spliteratorClass,
                TypicalTypes.MAP_OF_STRING_AND_OBJECTS);
        final ParameterizedTypeName consumerType = ParameterizedTypeName.get(TypicalTypes.CONSUMER,
                WildcardTypeName.supertypeOf(TypicalTypes.MAP_OF_STRING_AND_OBJECTS));
        final TypeSpec spliterator = TypeSpec
                .anonymousClassBuilder("$T.MAX_VALUE, $T.ORDERED", Long.class, Spliterator.class)
                .addSuperinterface(superinterface)
                .addMethod(TypicalMethods.implementation("tryAdvance")
                        .addParameter(TypicalParameters.parameter(consumerType, "action"))
                        .returns(boolean.class)
                        .beginControlFlow("try")
                        .beginControlFlow("if ($N.next())", "result")
                        .addStatement("$N.accept(resultSetToMap($N))", "action", "result")
                        .addStatement("return true")
                        .endControlFlow()
                        .addStatement("return false")
                        .endControlFlow()
                        .addCode(catchAndRethrow())
                        .build())
                .build();
        final TypeSpec closer = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(TypicalMethods.implementation("run")
                        .returns(void.class)
                        .beginControlFlow("try")
                        .addStatement("$N.close()", TypicalNames.RESULT_SET)
                        .addStatement("$N.close()", TypicalNames.PREPARED_STATEMENT)
                        .addStatement("$N.close()", TypicalNames.CONNECTION)
                        .endControlFlow()
                        .addCode(catchAndRethrow())
                        .build())
                .build();
        return MethodSpec.methodBuilder(configuration.getStreamLazyName())
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .returns(TypicalTypes.STREAM_OF_MAPS)
                .addParameters(configuration.getParameterSpecs())
                .beginControlFlow("try")
                .addCode(allPreparedAutoCloseableResources(configuration))
                .addCode(setParameters(configuration))
                .addCode(TypicalCodeBlocks.executeQuery())
                .addCode(TypicalCodeBlocks.getMetaData())
                .addCode(TypicalCodeBlocks.getColumnCount())
                .addStatement("final $T $N = new $T($N, $N, $N)", resultState, "result", resultState,
                        TypicalNames.RESULT_SET, TypicalNames.META_DATA, TypicalNames.COLUMN_COUNT)
                .addStatement("return $T.stream($L, false).onClose($L)",
                        StreamSupport.class, spliterator, closer)
                .endControlFlow()
                .addCode(catchAndRethrow())
                .build();
    }

    private MethodSpec resultSetToList() {
        return MethodSpec.methodBuilder("resultSetToList")
                .addModifiers(TypicalModifiers.PRIVATE_METHOD)
                .addParameter(TypicalParameters.parameter(resultState, "result"))
                .addException(SQLException.class)
                .returns(TypicalTypes.LIST_OF_MAPS)
                .addStatement("final $T list = new $T<>()", TypicalTypes.LIST_OF_MAPS, ArrayList.class)
                .beginControlFlow("while ($N.next())", "result")
                .addStatement("$N.add($N($N))", "list", "resultSetToMap", "result")
                .endControlFlow()
                .addStatement("return $N", "list")
                .build();
    }

    private MethodSpec resultSetToMap() {
        return MethodSpec.methodBuilder("resultSetToMap")
                .addModifiers(TypicalModifiers.PRIVATE_METHOD)
                .addParameter(TypicalParameters.parameter(resultState, "result"))
                .addException(SQLException.class)
                .returns(TypicalTypes.MAP_OF_STRING_AND_OBJECTS)
                .addStatement("final $T row = new $T<>($N.getColumnCount())", TypicalTypes.MAP_OF_STRING_AND_OBJECTS,
                        LinkedHashMap.class, "result")
                .beginControlFlow("for (int index = 1; index <= $N.getColumnCount(); index++)",
                        "result")
                .addStatement("$N.put($N.getColumnName($N), $N.getObject($N))", "row", "result", "index",
                        "result", "index")
                .endControlFlow()
                .addStatement("return $N", "row")
                .build();
    }

    private static CodeBlock tryAllPreparedAutoCloseableResources(final SqlStatementConfiguration configuration) {
        return CodeBlock.builder()
                .beginControlFlow("try ($L)", allPreparedAutoCloseableResources(configuration))
                .build();
    }

    private static CodeBlock allPreparedAutoCloseableResources(final SqlStatementConfiguration configuration) {
        return CodeBlock.builder()
                .addStatement("final $T $N = $N.getConnection()", Connection.class, TypicalNames.CONNECTION,
                        TypicalNames.DATA_SOURCE)
                .addStatement("final $T $N = $N.prepareStatement($N)", PreparedStatement.class,
                        TypicalNames.PREPARED_STATEMENT, TypicalNames.CONNECTION,
                        constantSqlStatementField(configuration))
                .build();
    }

    private static String constantSqlStatementField(final SqlStatementConfiguration configuration) {
        return configuration.getName().replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
    }

    private static CodeBlock setParameters(final SqlStatementConfiguration configuration) {
        return parameterAssignment(configuration, "$N.setObject($N, $N)",
                parameterName -> new String[] { TypicalNames.PREPARED_STATEMENT,
                        TypicalNames.JDBC_INDEX, parameterName });
    }

    private static CodeBlock setBatchParameters(final SqlStatementConfiguration configuration) {
        return parameterAssignment(configuration, "$N.setObject($N, $N[$N])",
                parameterName -> new String[] { TypicalNames.PREPARED_STATEMENT,
                        TypicalNames.JDBC_INDEX, parameterName, TypicalNames.BATCH });
    }

    private static CodeBlock parameterAssignment(
            final SqlStatementConfiguration configuration,
            final String codeStatement,
            final Function<String, Object[]> parameterSetter) {
        final com.squareup.javapoet.CodeBlock.Builder builder = CodeBlock.builder();
        final List<SqlParameter> parameters = configuration.getParameters();
        if (parameters != null && !parameters.isEmpty()) {
            for (final SqlParameter parameter : configuration.getParameters()) {
                builder.beginControlFlow("for (final int $N : $N.get($S))", TypicalNames.JDBC_INDEX,
                        constsantSqlStatementParametersField(configuration), parameter.getName())
                        .add(CodeBlock.builder().addStatement(codeStatement,
                                parameterSetter.apply(parameter.getName())).build())
                        .endControlFlow();
            }
        }
        return builder.build();
    }

    private static CodeBlock catchAndRethrow() {
        return CodeBlock.builder()
                .beginControlFlow("catch (final $T $N)", SQLException.class, TypicalNames.EXCEPTION)
                .addStatement("throw new $T($N)", RuntimeException.class, TypicalNames.EXCEPTION)
                .endControlFlow()
                .build();
    }

}
