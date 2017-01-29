package com.github.sebhoss.yosql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
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
import com.github.sebhoss.yosql.generator.TypicalModifiers;
import com.github.sebhoss.yosql.generator.TypicalNames;
import com.github.sebhoss.yosql.generator.TypicalTypes;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.functions.BiConsumer;

@Named
@Singleton
public class CodeGenerator {

    private static final ClassName    string         = ClassName.get("java.lang", "String");
    private static final ClassName    object         = ClassName.get("java.lang", "Object");
    private static final ClassName    list           = ClassName.get("java.util", "List");
    private static final ClassName    map            = ClassName.get("java.util", "Map");
    private static final ClassName    stream         = ClassName.get("java.util.stream", "Stream");
    private static final ClassName    flowable       = ClassName.get("io.reactivex", "Flowable");
    private static final TypeName     mapOfKeyValues = ParameterizedTypeName.get(map, string, object);
    private static final TypeName     listOfMaps     = ParameterizedTypeName.get(list, mapOfKeyValues);
    private static final TypeName     streamOfMaps   = ParameterizedTypeName.get(stream, mapOfKeyValues);
    private static final TypeName     flowableOfMaps = ParameterizedTypeName.get(flowable, mapOfKeyValues);

    private final PluginErrors        pluginErrors;
    private final FlowStateGenerator  flowStateGenerator;
    private final PluginRuntimeConfig runtimeConfig;

    @Inject
    public CodeGenerator(
            final FlowStateGenerator flowStateGenerator,
            final PluginErrors pluginErrors,
            final PluginRuntimeConfig runtimeConfig) {
        this.flowStateGenerator = flowStateGenerator;
        this.pluginErrors = pluginErrors;
        this.runtimeConfig = runtimeConfig;
    }

    public void generateUtilities(final List<SqlStatement> allStatements) {
        if (allStatements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlStatementType.READING == config.getType())
                .anyMatch(config -> config.isGenerateRxJavaApi())) {
            flowStateGenerator.generateFlowStateClass();
        }
    }

    /**
     * Generates a single repository.
     *
     * @param name
     * @param sqlStatements
     *            The SQL statements to be included in the repository.
     */
    public void generateRepository(final String fqnOfRepository,
            final List<SqlStatement> sqlStatements) {
        final String className = getClassName(fqnOfRepository);
        final String packageName = getPackageName(fqnOfRepository);
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

    private String getPackageName(final String fqnOfRepository) {
        return fqnOfRepository.substring(0, fqnOfRepository.lastIndexOf("."));
    }

    private String getClassName(final String fqnOfRepository) {
        return fqnOfRepository.substring(fqnOfRepository.lastIndexOf(".") + 1,
                fqnOfRepository.length());
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
                .filter(statement -> statement.getConfiguration().isSingle())
                .map(CodeGenerator::singleQuery);
        final Stream<MethodSpec> batchQueries = sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isBatch())
                .filter(statement -> statement.getConfiguration().hasParameters())
                .filter(statement -> SqlStatementType.WRITING == statement.getConfiguration().getType())
                .map(CodeGenerator::batchQuery);
        final Stream<MethodSpec> streamQueries = Stream.concat(sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isStreamEager())
                .filter(statement -> SqlStatementType.READING == statement.getConfiguration().getType())
                .map(CodeGenerator::streamQueryEager), sqlStatements.stream()
                        .filter(statement -> statement.getConfiguration().isStreamLazy())
                        .filter(statement -> SqlStatementType.READING == statement.getConfiguration().getType())
                        .map(CodeGenerator::streamQueryLazy));
        final Stream<MethodSpec> rxJavaQueries = sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isGenerateRxJavaApi())
                .filter(statement -> SqlStatementType.READING == statement.getConfiguration().getType())
                .map(this::flowWithBackpressure);
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
        return MethodSpec.constructorBuilder()
                .addModifiers(TypicalModifiers.PUBLIC_CONSTRUCTOR)
                .addParameter(DataSource.class, TypicalNames.DATA_SOURCE, TypicalModifiers.PARAMETER)
                .addStatement("this.$N = $N", TypicalNames.DATA_SOURCE, TypicalNames.DATA_SOURCE)
                .build();
    }

    private boolean shouldBuildResultToMapHelperMethod(final SqlStatement statement) {
        return statement.getConfiguration().isStreamLazy()
                || shouldBuildResultToListHelperMethod(statement);
    }

    private boolean shouldBuildResultToListHelperMethod(final SqlStatement statement) {
        return statement.getConfiguration().isStreamEager()
                || statement.getConfiguration().isSingle()
                        && SqlStatementType.READING == statement.getConfiguration().getType();
    }

    private static MethodSpec singleQuery(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        if (SqlStatementType.READING == configuration.getType()) {
            final Builder methodBuilder = MethodSpec.methodBuilder(configuration.getName())
                    .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                    .returns(listOfMaps)
                    .addParameters(configuration.getParameterSpecs());
            if (configuration.getParameters() != null && !configuration.getParameters().isEmpty()) {
                methodBuilder.beginControlFlow("try ($L)", allPreparedAutoCloseableResources(configuration));
                for (final SqlParameter parameter : configuration.getParameters()) {
                    methodBuilder.beginControlFlow("for (final int $N : $N.get($S))", TypicalNames.JDBC_INDEX,
                            constsantSqlStatementParametersField(configuration), parameter.getName())
                            .addStatement("$N.setObject($N, $N)", TypicalNames.PREPARED_STATEMENT,
                                    TypicalNames.JDBC_INDEX,
                                    parameter.getName())
                            .endControlFlow();
                }
                methodBuilder
                        .beginControlFlow("try (final $T $N = $N.executeQuery())", ResultSet.class,
                                TypicalNames.RESULT_SET,
                                TypicalNames.PREPARED_STATEMENT)
                        .addStatement("return resultSetToList($N)", TypicalNames.RESULT_SET)
                        .endControlFlow()
                        .endControlFlow();
            } else {
                methodBuilder.beginControlFlow("try ($L)", allAutoCloseableResources(configuration))
                        .addStatement("return resultSetToList($N)", TypicalNames.RESULT_SET)
                        .endControlFlow();
            }
            return methodBuilder
                    .beginControlFlow("catch ($T $N)", SQLException.class, TypicalNames.EXCEPTION)
                    .addStatement("throw new $T($N)", RuntimeException.class, TypicalNames.EXCEPTION)
                    .endControlFlow()
                    .build();
        } else {
            final Builder methodBuilder = MethodSpec.methodBuilder(configuration.getName())
                    .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                    .returns(int.class)
                    .addParameters(configuration.getParameterSpecs());
            if (configuration.getParameters() != null && !configuration.getParameters().isEmpty()) {
                methodBuilder.beginControlFlow("try ($L)", allPreparedAutoCloseableResources(configuration));
                for (final SqlParameter parameter : configuration.getParameters()) {
                    methodBuilder.beginControlFlow("for (final int $N : $N.get($S))", TypicalNames.JDBC_INDEX,
                            constsantSqlStatementParametersField(configuration), parameter.getName())
                            .addStatement("$N.setObject($N, $N)", TypicalNames.PREPARED_STATEMENT,
                                    TypicalNames.JDBC_INDEX,
                                    parameter.getName())
                            .endControlFlow();
                }
                methodBuilder
                        .addStatement("return $N.executeUpdate()", TypicalNames.PREPARED_STATEMENT)
                        .endControlFlow();
            } else {
                methodBuilder.beginControlFlow("try ($L)", allPreparedAutoCloseableResources(configuration))
                        .addStatement("return $N.executeUpdate()", TypicalNames.PREPARED_STATEMENT)
                        .endControlFlow();
            }
            return methodBuilder
                    .beginControlFlow("catch ($T $N)", SQLException.class, TypicalNames.EXCEPTION)
                    .addStatement("throw new $T($N)", RuntimeException.class, TypicalNames.EXCEPTION)
                    .endControlFlow()
                    .build();
        }
    }

    private MethodSpec flowWithBackpressure(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();

        final ClassName flowState = ClassName.get(runtimeConfig.getUtilityPackageName(),
                FlowStateGenerator.FLOW_STATE_CLASS_NAME);
        final ClassName callable = ClassName.get(Callable.class);
        final ParameterizedTypeName initialStateType = ParameterizedTypeName.get(callable, flowState);
        final Builder callMethodBuilder = MethodSpec.methodBuilder("call")
                .addAnnotation(Override.class)
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .returns(flowState)
                .addException(Exception.class)
                .addStatement("final $T $N = new $T()", flowState, TypicalNames.STATE, flowState)
                .addStatement("$N.$N = $N.getConnection()", TypicalNames.STATE, TypicalNames.CONNECTION,
                        TypicalNames.DATA_SOURCE)
                .addStatement("$N.$N = $N.$N.prepareStatement($N)", TypicalNames.STATE,
                        TypicalNames.PREPARED_STATEMENT, TypicalNames.STATE, TypicalNames.CONNECTION,
                        constantSqlStatementField(configuration));
        for (final SqlParameter parameter : configuration.getParameters()) {
            callMethodBuilder.beginControlFlow("for (final int $N : $N.get($S))", TypicalNames.JDBC_INDEX,
                    constsantSqlStatementParametersField(configuration), parameter.getName())
                    .addStatement("$N.$N.setObject($N, $N)", TypicalNames.STATE, TypicalNames.PREPARED_STATEMENT,
                            TypicalNames.JDBC_INDEX, parameter.getName())
                    .endControlFlow();
        }
        final TypeSpec initialState = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(initialStateType)
                .addMethod(callMethodBuilder
                        .addStatement("$N.$N = $N.$N.executeQuery()", TypicalNames.STATE, TypicalNames.RESULT_SET,
                                TypicalNames.STATE, TypicalNames.PREPARED_STATEMENT)
                        .addStatement("$N.$N = $N.$N.getMetaData()", TypicalNames.STATE, TypicalNames.META_DATA,
                                TypicalNames.STATE, TypicalNames.RESULT_SET)
                        .addStatement("$N.$N = $N.$N.getColumnCount()", TypicalNames.STATE, TypicalNames.COLUMN_COUNT,
                                TypicalNames.STATE, TypicalNames.META_DATA)
                        .addStatement("return $N", TypicalNames.STATE)
                        .build())
                .build();

        final ClassName biConsumer = ClassName.get(BiConsumer.class);
        final ClassName rawEmitter = ClassName.get(Emitter.class);
        final ParameterizedTypeName emitter = ParameterizedTypeName.get(rawEmitter,
                TypicalTypes.MAP_OF_STRING_AND_OBJECTS);
        final ParameterizedTypeName generatorType = ParameterizedTypeName.get(biConsumer, flowState, emitter);
        final TypeSpec generator = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(generatorType)
                .addMethod(MethodSpec.methodBuilder("accept")
                        .addAnnotation(Override.class)
                        .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                        .addParameter(flowState, TypicalNames.STATE, TypicalModifiers.PARAMETER)
                        .addParameter(emitter, TypicalNames.EMITTER, TypicalModifiers.PARAMETER)
                        .returns(void.class)
                        .addException(Exception.class)
                        .beginControlFlow("try")
                        .beginControlFlow("if ($N.$N.next())", TypicalNames.STATE, TypicalNames.RESULT_SET)
                        .addStatement("$1N.onNext(resultSetToMap($2N.$3N, $2N.$4N, $2N.$5N))", TypicalNames.EMITTER,
                                TypicalNames.STATE, TypicalNames.RESULT_SET, TypicalNames.META_DATA,
                                TypicalNames.COLUMN_COUNT)
                        .nextControlFlow("else")
                        .addStatement("$N.onComplete()", TypicalNames.EMITTER)
                        .endControlFlow()
                        .endControlFlow()
                        .beginControlFlow("catch (final $T $N)", SQLException.class, TypicalNames.EXCEPTION)
                        .addStatement("$N.onError($N)", TypicalNames.EMITTER, TypicalNames.EXCEPTION)
                        .endControlFlow()
                        .build())
                .build();

        final ClassName consumerClass = ClassName.get(io.reactivex.functions.Consumer.class);
        final ParameterizedTypeName disposerType = ParameterizedTypeName.get(consumerClass, flowState);
        final TypeSpec disposer = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(disposerType)
                .addMethod(MethodSpec.methodBuilder("accept")
                        .addAnnotation(Override.class)
                        .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                        .addParameter(flowState, TypicalNames.STATE, TypicalModifiers.PARAMETER)
                        .returns(void.class)
                        .addException(Exception.class)
                        .beginControlFlow("try")
                        .addStatement("$N.$N.close()", TypicalNames.STATE, TypicalNames.RESULT_SET)
                        .addStatement("$N.$N.close()", TypicalNames.STATE, TypicalNames.PREPARED_STATEMENT)
                        .addStatement("$N.$N.close()", TypicalNames.STATE, TypicalNames.CONNECTION)
                        .endControlFlow()
                        .beginControlFlow("catch (final $T $N)", SQLException.class, TypicalNames.EXCEPTION)
                        .addStatement("throw new $T($N)", RuntimeException.class, TypicalNames.EXCEPTION)
                        .endControlFlow()
                        .build())
                .build();

        return MethodSpec.methodBuilder(configuration.getFlowableName())
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .returns(flowableOfMaps)
                .addParameters(configuration.getParameterSpecs())
                .addStatement("return $T.generate($L, $L, $L)",
                        Flowable.class, initialState, generator, disposer)
                .build();
    }

    private static MethodSpec batchQuery(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        final Builder methodBuilder = MethodSpec.methodBuilder(configuration.getBatchName())
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .returns(TypicalTypes.ARRAY_OF_INTS)
                .addParameters(configuration.getBatchParameterSpecs())
                .beginControlFlow("try ($L)", allPreparedAutoCloseableResources(configuration))
                .beginControlFlow("for (int $N = 0; $N < $N.length; $N++)", TypicalNames.BATCH, TypicalNames.BATCH,
                        configuration.getParameters().get(0).getName(), TypicalNames.BATCH);
        for (final SqlParameter parameter : configuration.getParameters()) {
            methodBuilder.beginControlFlow("for (final int $N : $N.get($S))", TypicalNames.JDBC_INDEX,
                    constsantSqlStatementParametersField(configuration), parameter.getName())
                    .addStatement("$N.setObject($N, $N[$N])", TypicalNames.PREPARED_STATEMENT,
                            TypicalNames.JDBC_INDEX, parameter.getName(), TypicalNames.BATCH)
                    .endControlFlow();
        }
        return methodBuilder
                .addStatement("$N.addBatch()", TypicalNames.PREPARED_STATEMENT)
                .endControlFlow()
                .addStatement("return $N.executeBatch()", TypicalNames.PREPARED_STATEMENT)
                .endControlFlow()
                .beginControlFlow("catch ($T $N)", SQLException.class, TypicalNames.EXCEPTION)
                .addStatement("throw new $T($N)", RuntimeException.class, TypicalNames.EXCEPTION)
                .endControlFlow()
                .build();
    }

    private static MethodSpec streamQueryEager(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        final Builder methodBuilder = MethodSpec.methodBuilder(configuration.getStreamEagerName())
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .returns(streamOfMaps)
                .addParameters(configuration.getParameterSpecs());
        if (configuration.getParameters() != null && !configuration.getParameters().isEmpty()) {
            methodBuilder.beginControlFlow("try ($L)", allPreparedAutoCloseableResources(configuration));
            for (final SqlParameter parameter : configuration.getParameters()) {
                methodBuilder.beginControlFlow("for (final int $N : $N.get($S))", TypicalNames.JDBC_INDEX,
                        constsantSqlStatementParametersField(configuration), parameter.getName())
                        .addStatement("$N.setObject($N, $N)", TypicalNames.PREPARED_STATEMENT, TypicalNames.JDBC_INDEX,
                                parameter.getName())
                        .endControlFlow();
            }
            methodBuilder
                    .beginControlFlow("try (final $T $N = $N.executeQuery())", ResultSet.class, TypicalNames.RESULT_SET,
                            TypicalNames.PREPARED_STATEMENT)
                    .addStatement("final $T resultList = resultSetToList($N)", listOfMaps, TypicalNames.RESULT_SET)
                    .addStatement("return $N.stream()", "resultList")
                    .endControlFlow()
                    .endControlFlow();
        } else {
            methodBuilder.beginControlFlow("try ($L)", allAutoCloseableResources(configuration))
                    .addStatement("final $T resultList = resultSetToList($N)", listOfMaps, TypicalNames.RESULT_SET)
                    .addStatement("return $N.stream()", "resultList")
                    .endControlFlow();
        }
        return methodBuilder
                .beginControlFlow("catch (final $T $N)", SQLException.class, TypicalNames.EXCEPTION)
                .addStatement("throw new $T($N)", RuntimeException.class, TypicalNames.EXCEPTION)
                .endControlFlow()
                .build();
    }

    private static CodeBlock allAutoCloseableResources(final SqlStatementConfiguration configuration) {
        final CodeBlock autocloseResources = CodeBlock.builder()
                .addStatement("final $T $N = $N.getConnection()", Connection.class, TypicalNames.CONNECTION,
                        TypicalNames.DATA_SOURCE)
                .addStatement("final $T $N = $N.prepareStatement($N)", PreparedStatement.class,
                        TypicalNames.PREPARED_STATEMENT, TypicalNames.CONNECTION,
                        constantSqlStatementField(configuration))
                .addStatement("final $T $N = $N.executeQuery()", ResultSet.class, TypicalNames.RESULT_SET,
                        TypicalNames.PREPARED_STATEMENT)
                .build();
        return autocloseResources;
    }

    private static CodeBlock allPreparedAutoCloseableResources(final SqlStatementConfiguration configuration) {
        final CodeBlock autocloseResources = CodeBlock.builder()
                .addStatement("final $T $N = $N.getConnection()", Connection.class, TypicalNames.CONNECTION,
                        TypicalNames.DATA_SOURCE)
                .addStatement("final $T $N = $N.prepareStatement($N)", PreparedStatement.class,
                        TypicalNames.PREPARED_STATEMENT, TypicalNames.CONNECTION,
                        constantSqlStatementField(configuration))
                .build();
        return autocloseResources;
    }

    private static MethodSpec streamQueryLazy(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        final ParameterizedTypeName map = ParameterizedTypeName.get(Map.class, String.class, Object.class);
        final ClassName spliteratorClass = ClassName.get(Spliterators.AbstractSpliterator.class);
        final ParameterizedTypeName superinterface = ParameterizedTypeName.get(spliteratorClass, map);
        final ClassName consumerClass = ClassName.get(Consumer.class);
        final ParameterizedTypeName consumerType = ParameterizedTypeName.get(consumerClass,
                WildcardTypeName.supertypeOf(map));
        final TypeSpec spliterator = TypeSpec
                .anonymousClassBuilder("$T.MAX_VALUE, $T.ORDERED", Long.class, Spliterator.class)
                .addSuperinterface(superinterface)
                .addMethod(MethodSpec.methodBuilder("tryAdvance")
                        .addAnnotation(Override.class)
                        .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                        .addParameter(consumerType, "action")
                        .returns(boolean.class)
                        .beginControlFlow("try")
                        .beginControlFlow("if ($N.next())", TypicalNames.RESULT_SET)
                        .addStatement("$N.accept(resultSetToMap($N, $N, $N))", "action", TypicalNames.RESULT_SET,
                                "metaData", "columnCount")
                        .addStatement("return true")
                        .endControlFlow()
                        .addStatement("return false")
                        .endControlFlow()
                        .beginControlFlow("catch (final $T $N)", SQLException.class, TypicalNames.EXCEPTION)
                        .addStatement("throw new $T($N)", RuntimeException.class, TypicalNames.EXCEPTION)
                        .endControlFlow()
                        .build())
                .build();
        final TypeSpec closer = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(MethodSpec.methodBuilder("run")
                        .addAnnotation(Override.class)
                        .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                        .returns(void.class)
                        .beginControlFlow("try")
                        .addStatement("$N.close()", TypicalNames.RESULT_SET)
                        .addStatement("$N.close()", TypicalNames.PREPARED_STATEMENT)
                        .addStatement("$N.close()", TypicalNames.CONNECTION)
                        .endControlFlow()
                        .beginControlFlow("catch ($T $N)", SQLException.class, TypicalNames.EXCEPTION)
                        .addStatement("throw new $T($N)", RuntimeException.class, TypicalNames.EXCEPTION)
                        .endControlFlow()
                        .build())
                .build();
        final Builder queryMethodBuilder = MethodSpec.methodBuilder(configuration.getStreamLazyName())
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .returns(streamOfMaps)
                .addParameters(configuration.getParameterSpecs())
                .beginControlFlow("try")
                .addStatement("final $T $N = $N.getConnection()", Connection.class, TypicalNames.CONNECTION,
                        TypicalNames.DATA_SOURCE)
                .addStatement("final $T $N = $N.prepareStatement($N)", PreparedStatement.class,
                        TypicalNames.PREPARED_STATEMENT, TypicalNames.CONNECTION,
                        constantSqlStatementField(configuration));
        if (configuration.getParameters() != null && !configuration.getParameters().isEmpty()) {
            for (final SqlParameter parameter : configuration.getParameters()) {
                queryMethodBuilder.beginControlFlow("for (final int $N : $N.get($S))", TypicalNames.JDBC_INDEX,
                        constsantSqlStatementParametersField(configuration), parameter.getName())
                        .addStatement("$N.setObject($N, $N)", TypicalNames.PREPARED_STATEMENT, TypicalNames.JDBC_INDEX,
                                parameter.getName())
                        .endControlFlow();
            }
        }
        return queryMethodBuilder
                .addStatement("final $T $N = $N.executeQuery()", ResultSet.class, TypicalNames.RESULT_SET,
                        TypicalNames.PREPARED_STATEMENT)
                .addStatement("final $T $N = $N.getMetaData()", ResultSetMetaData.class, TypicalNames.META_DATA,
                        TypicalNames.RESULT_SET)
                .addStatement("final int $N = $N.getColumnCount()", TypicalNames.COLUMN_COUNT, TypicalNames.META_DATA)
                .addStatement("return $T.stream($L, false).onClose($L)",
                        StreamSupport.class, spliterator, closer)
                .endControlFlow()
                .beginControlFlow("catch ($T $N)", SQLException.class, TypicalNames.EXCEPTION)
                .addStatement("throw new $T($N)", RuntimeException.class, TypicalNames.EXCEPTION)
                .endControlFlow()
                .build();
    }

    private static MethodSpec resultSetToList() {
        return MethodSpec.methodBuilder("resultSetToList")
                .addModifiers(TypicalModifiers.PRIVATE_METHOD)
                .addParameter(ResultSet.class, TypicalNames.RESULT_SET, TypicalModifiers.PARAMETER)
                .addException(SQLException.class)
                .returns(listOfMaps)
                .addStatement("final $T metaData = $N.getMetaData()", ResultSetMetaData.class, TypicalNames.RESULT_SET)
                .addStatement("final int columnCount = $N.getColumnCount()", TypicalNames.META_DATA)
                .addStatement("final $T list = new $T<>()", listOfMaps, ArrayList.class)
                .beginControlFlow("while ($N.next())", TypicalNames.RESULT_SET)
                .addStatement("$N.add($N($N, $N, $N))", "list", "resultSetToMap", TypicalNames.RESULT_SET,
                        TypicalNames.META_DATA, TypicalNames.COLUMN_COUNT)
                .endControlFlow()
                .addStatement("return $N", "list")
                .build();
    }

    private static MethodSpec resultSetToMap() {
        return MethodSpec.methodBuilder("resultSetToMap")
                .addModifiers(TypicalModifiers.PRIVATE_METHOD)
                .addParameter(ResultSet.class, TypicalNames.RESULT_SET, TypicalModifiers.PARAMETER)
                .addParameter(ResultSetMetaData.class, TypicalNames.META_DATA, TypicalModifiers.PARAMETER)
                .addParameter(int.class, TypicalNames.COLUMN_COUNT, TypicalModifiers.PARAMETER)
                .addException(SQLException.class)
                .returns(mapOfKeyValues)
                .addStatement("final $T row = new $T<>($N)", mapOfKeyValues, LinkedHashMap.class,
                        TypicalNames.COLUMN_COUNT)
                .beginControlFlow("for (int index = 1; index <= $N; index++)", TypicalNames.COLUMN_COUNT)
                .addStatement("$N.put($N.getColumnName($N), $N.getObject($N))", "row", TypicalNames.META_DATA, "index",
                        TypicalNames.RESULT_SET, "index")
                .endControlFlow()
                .addStatement("return $N", "row")
                .build();
    }

    private static String constantSqlStatementField(final SqlStatementConfiguration configuration) {
        return configuration.getName().replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
    }

}
