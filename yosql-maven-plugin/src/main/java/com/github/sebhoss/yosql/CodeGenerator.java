package com.github.sebhoss.yosql;

import java.io.IOException;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Generated;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.sql.DataSource;

import com.github.sebhoss.yosql.generator.FlowStateGenerator;
import com.github.sebhoss.yosql.generator.ResultRowGenerator;
import com.github.sebhoss.yosql.generator.ResultStateGenerator;
import com.github.sebhoss.yosql.generator.ToResultRowConverterGenerator;
import com.github.sebhoss.yosql.helpers.TypicalCodeBlocks;
import com.github.sebhoss.yosql.helpers.TypicalFields;
import com.github.sebhoss.yosql.helpers.TypicalMethods;
import com.github.sebhoss.yosql.helpers.TypicalModifiers;
import com.github.sebhoss.yosql.helpers.TypicalNames;
import com.github.sebhoss.yosql.helpers.TypicalParameters;
import com.github.sebhoss.yosql.helpers.TypicalTypes;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.CodeBlock.Builder;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import io.reactivex.Emitter;

@Named
@Singleton
public class CodeGenerator {

    private final PluginErrors                  pluginErrors;
    private final FlowStateGenerator            flowStateGenerator;
    private final PluginRuntimeConfig           runtimeConfig;
    private final ResultStateGenerator          resultStateGenerator;
    private final ToResultRowConverterGenerator toResultRowConverterGenerator;
    private final ResultRowGenerator            resultRowGenerator;
    private final TypicalCodeBlocks             codeBlocks;

    @Inject
    public CodeGenerator(
            final FlowStateGenerator flowStateGenerator,
            final ResultStateGenerator resultStateGenerator,
            final ToResultRowConverterGenerator toResultRowConverterGenerator,
            final ResultRowGenerator resultRowGenerator,
            final PluginErrors pluginErrors,
            final PluginRuntimeConfig runtimeConfig,
            final TypicalCodeBlocks codeBlocks) {
        this.flowStateGenerator = flowStateGenerator;
        this.resultStateGenerator = resultStateGenerator;
        this.toResultRowConverterGenerator = toResultRowConverterGenerator;
        this.resultRowGenerator = resultRowGenerator;
        this.pluginErrors = pluginErrors;
        this.runtimeConfig = runtimeConfig;
        this.codeBlocks = codeBlocks;
    }

    public void generateUtilities(final List<SqlStatement> allStatements) {
        if (allStatements.stream()
                .map(SqlStatement::getConfiguration)
                .anyMatch(config -> SqlStatementType.READING == config.getType())) {
            resultStateGenerator.generateResultStateClass();
        }
        if (allStatements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlStatementType.READING == config.getType())
                .anyMatch(config -> config.isGenerateRxJavaApi())) {
            flowStateGenerator.generateFlowStateClass();
        }
        if (resultConverters(allStatements)
                .anyMatch(converter -> converter.getConverterType().endsWith(
                        ToResultRowConverterGenerator.TO_RESULT_ROW_CONVERTER_CLASS_NAME))) {
            toResultRowConverterGenerator.generateToResultRowConverterClass();
            resultRowGenerator.generateResultRowClass();
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
                                    TypicalFields.constantSqlStatementParameterIndexFieldName(config),
                                    param.getName(),
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
                        .map(CodeGenerator::asConstantSqlParameterIndexField));
        final Stream<FieldSpec> fields = Stream.concat(Stream.of(asDataSourceField()),
                converterFields(sqlStatements));
        return Stream.concat(constants, fields)
                .collect(Collectors.toList());
    }

    private static FieldSpec asConstantSqlField(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        return FieldSpec.builder(String.class, TypicalFields.constantSqlStatementFieldName(configuration))
                .addModifiers(TypicalModifiers.CONSTANT_FIELD)
                .initializer("$S", replaceNamedParameters(sqlStatement))
                .build();
    }

    private static String replaceNamedParameters(final SqlStatement sqlStatement) {
        return sqlStatement.getStatement().replaceAll(SqlFileParser.PATTERN.pattern(), "?");
    }

    private static FieldSpec asConstantSqlParameterIndexField(final SqlStatement sqlStatement) {
        final SqlStatementConfiguration configuration = sqlStatement.getConfiguration();
        return FieldSpec.builder(TypicalTypes.MAP_OF_STRING_AND_NUMBERS,
                TypicalFields.constantSqlStatementParameterIndexFieldName(configuration))
                .addModifiers(TypicalModifiers.CONSTANT_FIELD)
                .initializer("new $T<>($L)", HashMap.class, sqlStatement.getConfiguration().getParameters().size())
                .build();
    }

    private static FieldSpec asDataSourceField() {
        return FieldSpec.builder(DataSource.class, TypicalNames.DATA_SOURCE)
                .addModifiers(TypicalModifiers.PRIVATE_FIELD)
                .build();
    }

    private Stream<FieldSpec> converterFields(final List<SqlStatement> sqlStatements) {
        return resultConverters(sqlStatements)
                .map(converter -> {
                    final ClassName converterClass = ClassName.bestGuess(converter.getConverterType());
                    return FieldSpec.builder(converterClass, converter.getAlias())
                            .addModifiers(TypicalModifiers.PRIVATE_FIELD)
                            .build();
                });
    }

    private Iterable<MethodSpec> asMethods(final List<SqlStatement> sqlStatements) {
        final List<MethodSpec> methods = new ArrayList<>(sqlStatements.size());

        methods.add(constructor(sqlStatements));
        sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isGenerateStandardApi())
                .filter(statement -> SqlStatementType.READING == statement.getConfiguration().getType())
                .collect(Collectors.groupingBy(statement -> statement.getConfiguration().getName()))
                .forEach((methodName, statements) -> methods.add(standardReadApi(methodName, statements)));
        sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isGenerateStandardApi())
                .filter(statement -> SqlStatementType.WRITING == statement.getConfiguration().getType())
                .collect(Collectors.groupingBy(statement -> statement.getConfiguration().getName()))
                .forEach((methodName, statements) -> methods.add(standardWriteApi(methodName, statements)));
        sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isGenerateBatchApi())
                .filter(statement -> statement.getConfiguration().hasParameters())
                .filter(statement -> SqlStatementType.WRITING == statement.getConfiguration().getType())
                .collect(Collectors.groupingBy(statement -> statement.getConfiguration().getName()))
                .forEach((methodName, statements) -> methods.add(batchApi(statements)));
        sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isGenerateStreamEagerApi())
                .filter(statement -> SqlStatementType.READING == statement.getConfiguration().getType())
                .collect(Collectors.groupingBy(statement -> statement.getConfiguration().getName()))
                .forEach((methodName, statements) -> methods.add(streamEagerApi(statements)));
        sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isGenerateStreamLazyApi())
                .filter(statement -> SqlStatementType.READING == statement.getConfiguration().getType())
                .collect(Collectors.groupingBy(statement -> statement.getConfiguration().getName()))
                .forEach((methodName, statements) -> methods.add(streamLazyApi(statements)));
        sqlStatements.stream()
                .filter(statement -> statement.getConfiguration().isGenerateRxJavaApi())
                .filter(statement -> SqlStatementType.READING == statement.getConfiguration().getType())
                .collect(Collectors.groupingBy(statement -> statement.getConfiguration().getName()))
                .forEach((methodName, statements) -> methods.add(rxJava2(statements)));

        return methods;
    }

    private MethodSpec constructor(final List<SqlStatement> sqlStatements) {
        final Builder builder = CodeBlock.builder();
        resultConverters(sqlStatements).forEach(converter -> {
            final ClassName converterClass = ClassName.bestGuess(converter.getConverterType());
            builder.addStatement("this.$N = new $T()", converter.getAlias(), converterClass);
        });

        return TypicalMethods.constructor()
                .addParameter(TypicalParameters.dataSource())
                .addCode(TypicalCodeBlocks.setFieldToSelf(TypicalNames.DATA_SOURCE))
                .addCode(builder.build())
                .build();
    }

    private Stream<ResultRowConverter> resultConverters(final List<SqlStatement> sqlStatements) {
        return sqlStatements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(config -> SqlStatementType.READING == config.getType())
                .map(SqlStatementConfiguration::getResultConverter)
                .filter(Objects::nonNull)
                .distinct();
    }

    private MethodSpec standardReadApi(final String methodName, final List<SqlStatement> sqlStatements) {
        final SqlStatementConfiguration configuration = SqlStatementConfiguration.merge(sqlStatements);
        final ResultRowConverter converter = configuration.getResultConverter();
        final ClassName resultType = ClassName.bestGuess(converter.getResultType());
        final ParameterizedTypeName listOfResults = ParameterizedTypeName.get(TypicalTypes.LIST, resultType);
        return TypicalMethods.publicMethod(methodName)
                .returns(listOfResults)
                .addParameters(configuration.getParameterSpecs())
                .addExceptions(sqlException(configuration))
                .addCode(tryConnect())
                .addCode(pickVendorQuery(sqlStatements))
                .addCode(tryPrepare())
                .addCode(setParameters(configuration))
                .addCode(tryExecute())
                .addCode(TypicalCodeBlocks.getMetaData())
                .addCode(TypicalCodeBlocks.getColumnCount())
                .addCode(codeBlocks.newResultState())
                .addStatement("final $T $N = new $T<>()", listOfResults, TypicalNames.LIST, ArrayList.class)
                .beginControlFlow("while ($N.next())", TypicalNames.RESULT)
                .addStatement("$N.add($N.asUserType($N))", TypicalNames.LIST, converter.getAlias(), TypicalNames.RESULT)
                .endControlFlow()
                .addStatement("return $N", TypicalNames.LIST)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addCode(maybeCatchAndRethrow(configuration))
                .build();
    }

    private CodeBlock pickVendorQuery(final List<SqlStatement> sqlStatements) {
        final Builder builder = CodeBlock.builder();
        if (sqlStatements.size() > 1) {
            builder.addStatement("final $T $N = $N.getMetaData().getDatabaseProductName()",
                    String.class, TypicalNames.DATABASE_PRODUCT_NAME, TypicalNames.CONNECTION)
                    .addStatement("$T $N = null", String.class, TypicalNames.QUERY)
                    .addStatement("$T $N = null", TypicalTypes.MAP_OF_STRING_AND_NUMBERS, TypicalNames.INDEX)
                    .beginControlFlow("switch ($N)", TypicalNames.DATABASE_PRODUCT_NAME);
            sqlStatements.stream()
                    .map(SqlStatement::getConfiguration)
                    .filter(config -> Objects.nonNull(config.getVendor()))
                    .forEach(config -> {
                        builder.add("case $S:\n", config.getVendor())
                                .addStatement("$N = $N", TypicalNames.QUERY,
                                        TypicalFields.constantSqlStatementFieldName(config));
                        if (config.hasParameters()) {
                            builder.addStatement("$N = $N", TypicalNames.INDEX,
                                    TypicalFields.constantSqlStatementParameterIndexFieldName(config));
                        }
                        builder.addStatement("break");
                    });
            sqlStatements.stream()
                    .map(SqlStatement::getConfiguration)
                    .filter(config -> Objects.isNull(config.getVendor()))
                    .limit(1)
                    .forEach(config -> {
                        builder.add("default:\n")
                                .addStatement("$N = $N", TypicalNames.QUERY,
                                        TypicalFields.constantSqlStatementFieldName(config));
                        if (config.hasParameters()) {
                            builder.addStatement("$N = $N", TypicalNames.INDEX,
                                    TypicalFields.constantSqlStatementParameterIndexFieldName(config));
                        }
                        builder.addStatement("break");
                    });
            builder.endControlFlow();
        } else {
            final SqlStatementConfiguration configuration = sqlStatements.get(0).getConfiguration();
            builder.addStatement("final $T $N = $N", String.class, TypicalNames.QUERY,
                    TypicalFields.constantSqlStatementFieldName(configuration));
            if (configuration.hasParameters()) {
                builder.addStatement("final $T $N = $N", TypicalTypes.MAP_OF_STRING_AND_NUMBERS, TypicalNames.INDEX,
                        TypicalFields.constantSqlStatementParameterIndexFieldName(configuration));
            }
        }
        return builder.build();
    }

    private MethodSpec standardWriteApi(final String methodName, final List<SqlStatement> sqlStatements) {
        final SqlStatementConfiguration configuration = SqlStatementConfiguration.merge(sqlStatements);
        return TypicalMethods.publicMethod(configuration.getName())
                .returns(int.class)
                .addExceptions(sqlException(configuration))
                .addParameters(configuration.getParameterSpecs())
                .addCode(tryConnect())
                .addCode(pickVendorQuery(sqlStatements))
                .addCode(tryPrepare())
                .addCode(setParameters(configuration))
                .addStatement("return $N.executeUpdate()", TypicalNames.PREPARED_STATEMENT)
                .endControlFlow()
                .endControlFlow()
                .addCode(maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec rxJava2(final List<SqlStatement> sqlStatements) {
        final SqlStatementConfiguration configuration = SqlStatementConfiguration.merge(sqlStatements);
        final ResultRowConverter converter = configuration.getResultConverter();
        final ClassName resultType = ClassName.bestGuess(converter.getResultType());
        final ParameterizedTypeName flowReturn = ParameterizedTypeName.get(TypicalTypes.FLOWABLE, resultType);

        final TypeSpec initialState = createFlowState(configuration, sqlStatements);
        final TypeSpec generator = createFlowGenerator(converter);
        final TypeSpec disposer = createFlowDisposer();

        return TypicalMethods.publicMethod(configuration.getFlowableName())
                .returns(flowReturn)
                .addParameters(configuration.getParameterSpecs())
                .addCode(TypicalCodeBlocks.newFlowable(initialState, generator, disposer))
                .build();
    }

    private TypeSpec createFlowState(final SqlStatementConfiguration configuration,
            final List<SqlStatement> sqlStatements) {
        final ClassName callable = ClassName.get(Callable.class);
        final ParameterizedTypeName initialStateType = ParameterizedTypeName.get(callable,
                runtimeConfig.getFlowStateClass());
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(initialStateType)
                .addMethod(TypicalMethods.implementation("call")
                        .returns(runtimeConfig.getFlowStateClass())
                        .addException(Exception.class)
                        .addCode(TypicalCodeBlocks.getConnection())
                        .addCode(pickVendorQuery(sqlStatements))
                        .addCode(TypicalCodeBlocks.prepareStatement())
                        .addCode(setParameters(configuration))
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
                        .addCode(startTryBlock())
                        .beginControlFlow("if ($N.next())", TypicalNames.STATE)
                        .addStatement("$N.onNext($N.asUserType($N))", TypicalNames.EMITTER, converter.getAlias(),
                                TypicalNames.STATE)
                        .nextControlFlow("else")
                        .addStatement("$N.onComplete()", TypicalNames.EMITTER)
                        .endControlFlow()
                        .endControlFlow()
                        .addCode(catchAndDo("$N.onError($N)", TypicalNames.EMITTER, TypicalNames.EXCEPTION))
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
                        .addParameter(
                                TypicalParameters.parameter(runtimeConfig.getFlowStateClass(), TypicalNames.STATE))
                        .returns(void.class)
                        .addException(Exception.class)
                        .addStatement("$N.close()", TypicalNames.STATE)
                        .build())
                .build();
    }

    private MethodSpec batchApi(final List<SqlStatement> sqlStatements) {
        final SqlStatementConfiguration configuration = SqlStatementConfiguration.merge(sqlStatements);
        return MethodSpec.methodBuilder(configuration.getBatchName())
                .addModifiers(TypicalModifiers.PUBLIC_METHOD)
                .returns(TypicalTypes.ARRAY_OF_INTS)
                .addParameters(configuration.getBatchParameterSpecs())
                .addExceptions(sqlException(configuration))
                .addCode(tryConnect())
                .addCode(pickVendorQuery(sqlStatements))
                .addCode(tryPrepare())
                .beginControlFlow("for (int $N = 0; $N < $N.length; $N++)", TypicalNames.BATCH, TypicalNames.BATCH,
                        configuration.getParameters().get(0).getName(), TypicalNames.BATCH)
                .addCode(setBatchParameters(configuration))
                .addStatement("$N.addBatch()", TypicalNames.PREPARED_STATEMENT)
                .endControlFlow()
                .addStatement("return $N.executeBatch()", TypicalNames.PREPARED_STATEMENT)
                .endControlFlow()
                .endControlFlow()
                .addCode(maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec streamEagerApi(final List<SqlStatement> sqlStatements) {
        final SqlStatementConfiguration configuration = SqlStatementConfiguration.merge(sqlStatements);
        final ResultRowConverter converter = configuration.getResultConverter();
        final ClassName resultType = ClassName.bestGuess(converter.getResultType());
        final ParameterizedTypeName listOfResults = ParameterizedTypeName.get(TypicalTypes.LIST, resultType);
        final ParameterizedTypeName streamOfResults = ParameterizedTypeName.get(TypicalTypes.STREAM, resultType);
        return TypicalMethods.publicMethod(configuration.getStreamEagerName())
                .returns(streamOfResults)
                .addParameters(configuration.getParameterSpecs())
                .addExceptions(sqlException(configuration))
                .addCode(tryConnect())
                .addCode(pickVendorQuery(sqlStatements))
                .addCode(tryPrepare())
                .addCode(setParameters(configuration))
                .addCode(tryExecute())
                .addCode(TypicalCodeBlocks.getMetaData())
                .addCode(TypicalCodeBlocks.getColumnCount())
                .addCode(codeBlocks.newResultState())
                .addStatement("final $T $N = new $T<>()", listOfResults, TypicalNames.LIST, ArrayList.class)
                .beginControlFlow("while ($N.next())", TypicalNames.RESULT)
                .addStatement("$N.add($N.asUserType($N))", TypicalNames.LIST, converter.getAlias(), TypicalNames.RESULT)
                .endControlFlow()
                .addStatement("return $N.stream()", TypicalNames.LIST)
                .endControlFlow()
                .endControlFlow()
                .endControlFlow()
                .addCode(maybeCatchAndRethrow(configuration))
                .build();
    }

    private MethodSpec streamLazyApi(final List<SqlStatement> sqlStatements) {
        final SqlStatementConfiguration configuration = SqlStatementConfiguration.merge(sqlStatements);
        final ResultRowConverter converter = configuration.getResultConverter();
        final ClassName resultType = ClassName.bestGuess(converter.getResultType());
        final ParameterizedTypeName streamOfResults = ParameterizedTypeName.get(TypicalTypes.STREAM, resultType);
        return TypicalMethods.publicMethod(configuration.getStreamLazyName())
                .returns(streamOfResults)
                .addParameters(configuration.getParameterSpecs())
                .addExceptions(sqlException(configuration))
                .addCode(maybeTry(configuration))
                .addCode(TypicalCodeBlocks.getConnection())
                .addCode(pickVendorQuery(sqlStatements))
                .addCode(TypicalCodeBlocks.prepareStatement())
                .addCode(setParameters(configuration))
                .addCode(TypicalCodeBlocks.executeQuery())
                .addCode(TypicalCodeBlocks.getMetaData())
                .addCode(TypicalCodeBlocks.getColumnCount())
                .addCode(codeBlocks.newResultState())
                .addCode(TypicalCodeBlocks.streamStatefull(lazyStreamSpliterator(converter), lazyStreamCloser()))
                .addCode(endMaybeTry(configuration))
                .addCode(maybeCatchAndRethrow(configuration))
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
                        .addCode(startTryBlock())
                        .beginControlFlow("if ($N.next())", TypicalNames.RESULT)
                        .addStatement("$N.accept($N.asUserType($N))", TypicalNames.ACTION, converter.getAlias(),
                                TypicalNames.RESULT)
                        .addStatement("return $L", true)
                        .endControlFlow()
                        .addStatement("return $L", false)
                        .endControlFlow()
                        .addCode(catchAndRethrow())
                        .build())
                .build();
    }

    private TypeSpec lazyStreamCloser() {
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(Runnable.class)
                .addMethod(TypicalMethods.implementation("run")
                        .returns(void.class)
                        .addCode(startTryBlock())
                        .addStatement("$N.close()", TypicalNames.RESULT_SET)
                        .addStatement("$N.close()", TypicalNames.PREPARED_STATEMENT)
                        .addStatement("$N.close()", TypicalNames.CONNECTION)
                        .endControlFlow()
                        .addCode(catchAndRethrow())
                        .build())
                .build();
    }

    private static CodeBlock tryExecute() {
        return CodeBlock.builder()
                .beginControlFlow("try ($L)", TypicalCodeBlocks.executeQuery())
                .build();
    }

    private static CodeBlock tryConnect() {
        return CodeBlock.builder()
                .beginControlFlow("try ($L)", TypicalCodeBlocks.getConnection())
                .build();
    }

    private static CodeBlock tryPrepare() {
        return CodeBlock.builder()
                .beginControlFlow("try ($L)", TypicalCodeBlocks.prepareStatement())
                .build();
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
                        TypicalNames.INDEX, parameter.getName())
                        .add(CodeBlock.builder().addStatement(codeStatement,
                                parameterSetter.apply(parameter.getName())).build())
                        .endControlFlow();
            }
        }
        return builder.build();
    }

    private CodeBlock maybeTry(final SqlStatementConfiguration configuration) {
        if (configuration.isMethodCatchAndRethrow()) {
            return startTryBlock();
        }
        return CodeBlock.builder().build();
    }

    private CodeBlock endMaybeTry(final SqlStatementConfiguration configuration) {
        if (configuration.isMethodCatchAndRethrow()) {
            return endTryBlock();
        }
        return CodeBlock.builder().build();
    }

    private CodeBlock startTryBlock() {
        return CodeBlock.builder().beginControlFlow("try").build();
    }

    private CodeBlock endTryBlock() {
        return CodeBlock.builder().endControlFlow().build();
    }

    private CodeBlock maybeCatchAndRethrow(final SqlStatementConfiguration configuration) {
        if (configuration.isMethodCatchAndRethrow()) {
            return catchAndRethrow();
        }
        return CodeBlock.builder().build();
    }

    private CodeBlock catchAndRethrow() {
        return catchAndDo("throw new $T($N)", RuntimeException.class, TypicalNames.EXCEPTION);
    }

    private CodeBlock catchAndDo(final String format, final Object... args) {
        return CodeBlock.builder()
                .beginControlFlow("catch (final $T $N)", SQLException.class, TypicalNames.EXCEPTION)
                .addStatement(format, args)
                .endControlFlow().build();
    }

    private Iterable<TypeName> sqlException(final SqlStatementConfiguration configuration) {
        if (!configuration.isMethodCatchAndRethrow()) {
            return Arrays.asList(ClassName.get(SQLException.class));
        }
        return Collections.emptyList();
    }

}
