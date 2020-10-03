/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.model.configuration;

import com.squareup.javapoet.ClassName;
import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.i18n.Translator;
import wtf.metio.yosql.model.options.*;

import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import java.nio.charset.Charset;
import java.nio.file.Paths;

import static java.lang.Boolean.parseBoolean;
import static wtf.metio.yosql.model.options.GenerateOptions.*;

/**
 * Provides the default execution configuration.
 */
@Module
public final class ModelConfigurationModule {

    private static final int SUPPORTS_GENERICS = 5;
    private static final int SUPPORTS_DIAMOND_OPERATOR = 7;
    private static final int SUPPORTS_STREAM_API = 8;
    private static final int SUPPORTS_PROCESSING_API = 9;
    private static final int SUPPORTS_VAR_KEYWORD = 11;
    private static final int SUPPORTS_TEXT_BLOCKS = 15;
    private static final int SUPPORTS_RECORDS = 16;
    private static final String SPLIT_LIST_ENTRIES_REGEX = ",";

    @Provides
    @Singleton
    JavaConfiguration provideJavaConfiguration(final Translator translator) {
        final var javaVersion = Integer.parseInt(translator.get(JAVA_DEFAULT));
        return JavaConfiguration.builder()
                .setTargetVersion(javaVersion)
                .setUseGenerics(javaVersion >= SUPPORTS_GENERICS)
                .setUseDiamondOperator(javaVersion >= SUPPORTS_DIAMOND_OPERATOR)
                .setUseStreamAPI(javaVersion >= SUPPORTS_STREAM_API)
                .setUseProcessingApi(javaVersion >= SUPPORTS_PROCESSING_API)
                .setUseVar(javaVersion >= SUPPORTS_VAR_KEYWORD)
                .setUseTextBlocks(javaVersion >= SUPPORTS_TEXT_BLOCKS)
                .setUseRecords(javaVersion >= SUPPORTS_RECORDS)
                .setUseFinal(parseBoolean(translator.get(USE_FINAL)))
                .build();
    }

    @Provides
    @Singleton
    AnnotationConfiguration provideAnnotationConfiguration(
            final JavaConfiguration javaConfiguration,
            final Translator translator) {
        final var annotationClass = javaConfiguration.useProcessingApi()
                ? AnnotationClassOptions.PROCESSING_API
                : AnnotationClassOptions.ANNOTATION_API;
        return AnnotationConfiguration.builder()
                .setGeneratorName(translator.get(GENERATOR_NAME))
                .setClassAnnotation(AnnotationClassOptions.valueOf(translator.get(annotationClass)))
                .setClassComment(translator.get(GENERATED_ANNOTATION_COMMENT_DEFAULT))
                .setClassMembers(AnnotationMemberOptions.WITHOUT_DATE)
                .setFieldAnnotation(AnnotationClassOptions.valueOf(translator.get(annotationClass)))
                .setFieldComment(translator.get(GENERATED_ANNOTATION_COMMENT_DEFAULT))
                .setFieldMembers(AnnotationMemberOptions.WITHOUT_DATE)
                .setMethodAnnotation(AnnotationClassOptions.valueOf(translator.get(annotationClass)))
                .setMethodComment(translator.get(GENERATED_ANNOTATION_COMMENT_DEFAULT))
                .setMethodMembers(AnnotationMemberOptions.WITHOUT_DATE)
                .build();
    }

    @Provides
    @Singleton
    VariableConfiguration provideVariableOptions(final JavaConfiguration javaConfiguration) {
        final var variableType = javaConfiguration.useVar()
                ? VariableTypeOptions.VAR
                : VariableTypeOptions.TYPE;
        return VariableConfiguration.builder()
                .addModifiers(Modifier.FINAL)
                .setVariableType(variableType)
                .build();
    }

    @Provides
    @Singleton
    JdbcNamesConfiguration provideJdbcNamesConfiguration(final Translator translator) {
        return JdbcNamesConfiguration.builder()
                .setDataSource(translator.get(JdbcNamesOptions.DATA_SOURCE))
                .setStatement(translator.get(JdbcNamesOptions.STATEMENT))
                .setRow(translator.get(JdbcNamesOptions.ROW))
                .setResultSet(translator.get(JdbcNamesOptions.RESULT_SET))
                .setMetaData(translator.get(JdbcNamesOptions.META_DATA))
                .setList(translator.get(JdbcNamesOptions.LIST))
                .setJdbcIndex(translator.get(JdbcNamesOptions.JDBC_INDEX))
                .setIndex(translator.get(JdbcNamesOptions.INDEX))
                .setConnection(translator.get(JdbcNamesOptions.CONNECTION))
                .setColumnLabel(translator.get(JdbcNamesOptions.COLUMN_LABEL))
                .setColumnCount(translator.get(JdbcNamesOptions.COLUMN_COUNT))
                .setBatch(translator.get(JdbcNamesOptions.BATCH))
                .build();
    }

    @Provides
    @Singleton
    JdbcFieldsConfiguration provideJdbcFieldsConfiguration(final Translator translator) {
        return JdbcFieldsConfiguration.builder()
                .setRawSuffix(translator.get(JdbcFieldsOptions.RAW_SUFFIX))
                .setIndexSuffix(translator.get(JdbcFieldsOptions.INDEX_SUFFIX))
                .build();
    }

    @Provides
    @Singleton
    StatementConfiguration provideStatementConfiguration(final JavaConfiguration javaConfiguration) {
        final var embed = javaConfiguration.useTextBlocks()
                ? StatementInRepositoryOptions.INLINE_TEXT_BLOCK
                : StatementInRepositoryOptions.INLINE_CONCAT;
        return StatementConfiguration.builder()
                .setEmbed(embed)
                .build();
    }

    @Provides
    @Singleton
    FileConfiguration providePathConfiguration(final Translator translator) {
        return FileConfiguration.builder()
                .setInputBaseDirectory(Paths.get(translator.get(CURRENT_DIRECTORY)))
                .setOutputBaseDirectory(Paths.get(translator.get(CURRENT_DIRECTORY)))
                .setSqlStatementSeparator(translator.get(SQL_STATEMENT_SEPARATOR_DEFAULT))
                .setSqlFilesSuffix(translator.get(SQL_FILES_SUFFIX_DEFAULT))
                .setSqlFilesCharset(Charset.forName(translator.get(SQL_FILES_CHARSET_DEFAULT)))
                .build();
    }

    @Provides
    @Singleton
    NameConfiguration provideNameConfiguration(final Translator translator) {
        return NameConfiguration.builder()
                .setBasePackageName(translator.get(BASE_PACKAGE_NAME_DEFAULT))
                .setUtilityPackageName(translator.get(UTILITY_PACKAGE_NAME_DEFAULT))
                .setConverterPackageName(translator.get(CONVERTER_PACKAGE_NAME_DEFAULT))
                .build();
    }

    @Provides
    @Singleton
    MethodConfiguration provideMethodConfiguration(final Translator translator) {
        return MethodConfiguration.builder()
                .setValidateMethodNamePrefixes(parseBoolean(translator.get(METHOD_VALIDATE_NAME_PREFIXES_DEFAULT)))
                .setMethodCatchAndRethrow(parseBoolean(translator.get(METHOD_CATCH_AND_RETHROW_DEFAULT)))
                .setGenerateStandardApi(parseBoolean(translator.get(METHOD_STANDARD_API_DEFAULT)))
                .setGenerateBatchApi(parseBoolean(translator.get(METHOD_BATCH_API_DEFAULT)))
                .setGenerateRxJavaApi(parseBoolean(translator.get(METHOD_RXJAVA_API_DEFAULT)))
                .setGenerateStreamEagerApi(parseBoolean(translator.get(METHOD_STREAM_EAGER_API_DEFAULT)))
                .setGenerateStreamLazyApi(parseBoolean(translator.get(METHOD_STREAM_LAZY_API_DEFAULT)))
                .setMethodBatchPrefix(translator.get(METHOD_BATCH_PREFIX_DEFAULT))
                .setMethodBatchSuffix(translator.get(METHOD_BATCH_SUFFIX_DEFAULT))
                .setMethodStreamPrefix(translator.get(METHOD_STREAM_PREFIX_DEFAULT))
                .setMethodStreamSuffix(translator.get(METHOD_STREAM_SUFFIX_DEFAULT))
                .setMethodRxJavaPrefix(translator.get(METHOD_RXJAVA_PREFIX_DEFAULT))
                .setMethodRxJavaSuffix(translator.get(METHOD_RXJAVA_SUFFIX_DEFAULT))
                .setMethodEagerName(translator.get(METHOD_EAGER_NAME_DEFAULT))
                .setMethodLazyName(translator.get(METHOD_LAZY_NAME_DEFAULT))
                .addAllowedCallPrefixes(translator.get(METHOD_ALLOWED_CALL_PREFIXES_DEFAULT)
                        .split(SPLIT_LIST_ENTRIES_REGEX))
                .addAllowedReadPrefixes(translator.get(METHOD_ALLOWED_READ_PREFIXES_DEFAULT)
                        .split(SPLIT_LIST_ENTRIES_REGEX))
                .addAllowedWritePrefixes(translator.get(METHOD_ALLOWED_WRITE_PREFIXES_DEFAULT)
                        .split(SPLIT_LIST_ENTRIES_REGEX))
                .build();
    }

    @Provides
    @Singleton
    ResourceConfiguration provideResourceConfiguration(final Translator translator) {
        return ResourceConfiguration.builder()
                .setMaxThreads(Integer.parseInt(translator.get(MAX_THREADS_DEFAULT)))
                .build();
    }

    @Provides
    @Singleton
    RepositoryConfiguration provideRepositoryConfiguration(final Translator translator) {
        return RepositoryConfiguration.builder()
                .setRepositoryNameSuffix(translator.get(REPOSITORY_NAME_SUFFIX_DEFAULT))
                .setRepositoryGenerateInterface(parseBoolean(translator.get(REPOSITORY_GENERATE_INTERFACE_DEFAULT)))
                .build();
    }

    @Provides
    @Singleton
    LoggingConfiguration provideLoggingConfiguration(final Translator translator) {
        return LoggingConfiguration.builder()
                .setApi(LoggingApiOptions.valueOf(translator.get(LOGGING_API_DEFAULT)))
                .build();
    }

    @Provides
    @Singleton
    RxJavaConfiguration provideRxJavaConfiguration(
            final Translator translator,
            final NameConfiguration names) {
        return RxJavaConfiguration.builder()
                .setFlowStateClass(ClassName.get(
                        names.basePackageName()
                                + "."
                                + names.utilityPackageName(),
                        translator.get(DEFAULT_FLOW_STATE_CLASS_NAME_DEFAULT)))
                .build();
    }

    @Provides
    @Singleton
    ResultConfiguration provideResultConfiguration(
            final Translator translator,
            final NameConfiguration names) {
        return ResultConfiguration.builder()
                .setResultStateClass(ClassName.get(
                        names.basePackageName()
                                + "."
                                + names.utilityPackageName(),
                        translator.get(DEFAULT_RESULT_STATE_CLASS_NAME_DEFAULT)))
                .setResultRowClass(ClassName.get(
                        names.basePackageName()
                                + "."
                                + names.utilityPackageName(),
                        translator.get(DEFAULT_RESULT_ROW_CLASS_NAME_DEFAULT)))
                .build();
    }

    @Provides
    @Singleton
    RuntimeConfiguration provideRuntimeConfiguration(
            final FileConfiguration files,
            final JavaConfiguration java,
            final AnnotationConfiguration annotations,
            final MethodConfiguration methods,
            final NameConfiguration names,
            final ResourceConfiguration resources,
            final RepositoryConfiguration repositories,
            final StatementConfiguration statements,
            final VariableConfiguration variables,
            final LoggingConfiguration logging,
            final RxJavaConfiguration rxJava) {
        return RuntimeConfiguration.builder()
                .setFiles(files)
                .setJava(java)
                .setAnnotations(annotations)
                .setMethods(methods)
                .setNames(names)
                .setResources(resources)
                .setRepositories(repositories)
                .setStatements(statements)
                .setVariables(variables)
                .setLogging(logging)
                .setRxJava(rxJava)
                .build();
    }

    /*
    @Provides
    @Singleton
    ExecutionConfiguration provideExecutionConfiguration(@NonLocalized final IMessageConveyor messages) {
        final var basePackageName = messages.getMessage(BASE_PACKAGE_NAME_DEFAULT);
        final var utilityPackageName = messages.getMessage(UTILITY_PACKAGE_NAME_DEFAULT);
        final var converterPackageName = messages.getMessage(CONVERTER_PACKAGE_NAME_DEFAULT);
        final var defaultRowConverterAlias = messages.getMessage(DEFAULT_ROW_CONVERTER_DEFAULT);
        final var defaultResultRowClassName = messages.getMessage(DEFAULT_RESULT_ROW_CLASS_NAME_DEFAULT);

        final var defaultResultRowConverter = new ResultRowConverter();
        defaultResultRowConverter.setAlias(defaultRowConverterAlias);
        defaultResultRowConverter.setResultType(String.join(".", basePackageName, utilityPackageName, defaultResultRowClassName));
        defaultResultRowConverter.setConverterType(String.join(".", basePackageName, converterPackageName,
                messages.getMessage(TO_RESULT_ROW_CONVERTER_CLASS_NAME)));

        return ExecutionConfiguration.builder()
                .setDefaultRowConverter(defaultRowConverterAlias)
                .setResultRowConverters(Collections.singletonList(defaultResultRowConverter))
                .build();
    }
    */

}
