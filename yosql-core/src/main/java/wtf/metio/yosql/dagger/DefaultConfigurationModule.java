/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dagger;

import com.squareup.javapoet.ClassName;
import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.i18n.Translator;
import wtf.metio.yosql.model.configuration.*;
import wtf.metio.yosql.model.options.*;

import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static wtf.metio.yosql.model.options.GenerateOptions.*;

/**
 * Provides the default execution configuration.
 */
@Module
public final class DefaultConfigurationModule {

    private static final int SUPPORTS_VAR_KEYWORD = 11;
    private static final String SPLIT_LIST_ENTRIES_REGEX = ",";

    @Provides
    @Singleton
    JavaConfiguration provideJavaConfiguration(final Translator translator) {
        return JavaConfiguration.builder()
                .setTargetVersion(Integer.parseInt(translator.nonLocalized(JAVA_DEFAULT)))
                .build();
    }

    @Provides
    @Singleton
    AnnotationConfiguration provideAnnotationConfiguration(final Translator translator) {
        return AnnotationConfiguration.builder()
                .setGeneratorName("YoSQL") // TODO: externalize
                .setClassAnnotation(AnnotationClassOptions.valueOf(translator.nonLocalized(AnnotationClassOptions.DEFAULT)))
                .setClassComment(translator.nonLocalized(GENERATED_ANNOTATION_COMMENT_DEFAULT))
                .setClassMembers(AnnotationMemberOptions.ALL)
                .setFieldAnnotation(AnnotationClassOptions.valueOf(translator.nonLocalized(AnnotationClassOptions.DEFAULT)))
                .setFieldComment(translator.nonLocalized(GENERATED_ANNOTATION_COMMENT_DEFAULT))
                .setFieldMembers(AnnotationMemberOptions.ALL)
                .setMethodAnnotation(AnnotationClassOptions.valueOf(translator.nonLocalized(AnnotationClassOptions.DEFAULT)))
                .setMethodComment(translator.nonLocalized(GENERATED_ANNOTATION_COMMENT_DEFAULT))
                .setMethodMembers(AnnotationMemberOptions.ALL)
                .build();
    }

    @Provides
    @Singleton
    VariableConfiguration provideVariableOptions(final JavaConfiguration javaConfiguration) {
        final var variableType = javaConfiguration.targetVersion() >= SUPPORTS_VAR_KEYWORD
                ? VariableTypeOptions.VAR
                : VariableTypeOptions.TYPE;
        return VariableConfiguration.builder()
                .setModifiers(List.of(Modifier.FINAL)) // TODO: externalize
                .setVariableType(variableType)
                .build();
    }

    @Provides
    @Singleton
    JdbcNamesConfiguration provideJdbcNamesConfiguration(final Translator translator) {
        return JdbcNamesConfiguration.builder()
                .setDataSource(translator.nonLocalized(JdbcNamesOptions.DATA_SOURCE))
                .build();
    }

    @Provides
    @Singleton
    JdbcFieldsConfiguration provideJdbcFieldsConfiguration() {
        return JdbcFieldsConfiguration.builder()
                .setRawSuffix("_RAW") // TODO: externalize
                .setIndexSuffix("_INDEX") // TODO: externalize
                .build();
    }

    @Provides
    @Singleton
    StatementConfiguration provideStatementConfiguration() {
        return StatementConfiguration.builder()
                .setEmbed(StatementInRepositoryOptions.INLINE_CONCAT)
                .build();
    }

    @Provides
    @Singleton
    FileConfiguration providePathConfiguration(final Translator translator) {
        return FileConfiguration.builder()
                .setInputBaseDirectory(Paths.get(translator.nonLocalized(CURRENT_DIRECTORY)))
                .setOutputBaseDirectory(Paths.get(translator.nonLocalized(CURRENT_DIRECTORY)))
                .setSqlStatementSeparator(translator.nonLocalized(SQL_STATEMENT_SEPARATOR_DEFAULT))
                .setSqlFilesSuffix(translator.nonLocalized(SQL_FILES_SUFFIX_DEFAULT))
                .setSqlFilesCharset(translator.nonLocalized(SQL_FILES_CHARSET_DEFAULT))
                .build();
    }

    @Provides
    @Singleton
    NameConfiguration provideNameConfiguration(final Translator translator) {
        return NameConfiguration.builder()
                .setBasePackageName(translator.nonLocalized(BASE_PACKAGE_NAME_DEFAULT))
                .setUtilityPackageName(translator.nonLocalized(UTILITY_PACKAGE_NAME_DEFAULT))
                .setConverterPackageName(translator.nonLocalized(CONVERTER_PACKAGE_NAME_DEFAULT))
                .build();
    }

    @Provides
    @Singleton
    MethodConfiguration provideMethodConfiguration(final Translator translator) {
        return MethodConfiguration.builder()
                .setValidateMethodNamePrefixes(Boolean.parseBoolean(
                        translator.nonLocalized(METHOD_VALIDATE_NAME_PREFIXES_DEFAULT)))
                .setMethodCatchAndRethrow(Boolean.parseBoolean(
                        translator.nonLocalized(METHOD_CATCH_AND_RETHROW_DEFAULT)))
                .setGenerateStandardApi(Boolean.parseBoolean(translator.nonLocalized(METHOD_STANDARD_API_DEFAULT)))
                .setGenerateBatchApi(Boolean.parseBoolean(translator.nonLocalized(METHOD_BATCH_API_DEFAULT)))
                .setGenerateRxJavaApi(Boolean.parseBoolean(translator.nonLocalized(METHOD_RXJAVA_API_DEFAULT)))
                .setGenerateStreamEagerApi(Boolean.parseBoolean(translator.nonLocalized(METHOD_STREAM_EAGER_API_DEFAULT)))
                .setGenerateStreamLazyApi(Boolean.parseBoolean(translator.nonLocalized(METHOD_STREAM_LAZY_API_DEFAULT)))
                .setMethodBatchPrefix(translator.nonLocalized(METHOD_BATCH_PREFIX_DEFAULT))
                .setMethodBatchSuffix(translator.nonLocalized(METHOD_BATCH_SUFFIX_DEFAULT))
                .setMethodStreamPrefix(translator.nonLocalized(METHOD_STREAM_PREFIX_DEFAULT))
                .setMethodStreamSuffix(translator.nonLocalized(METHOD_STREAM_SUFFIX_DEFAULT))
                .setMethodRxJavaPrefix(translator.nonLocalized(METHOD_RXJAVA_PREFIX_DEFAULT))
                .setMethodRxJavaSuffix(translator.nonLocalized(METHOD_RXJAVA_SUFFIX_DEFAULT))
                .setMethodEagerName(translator.nonLocalized(METHOD_EAGER_NAME_DEFAULT))
                .setMethodLazyName(translator.nonLocalized(METHOD_LAZY_NAME_DEFAULT))
                .setAllowedCallPrefixes(Arrays.asList(
                        translator.nonLocalized(METHOD_ALLOWED_CALL_PREFIXES_DEFAULT).split(SPLIT_LIST_ENTRIES_REGEX)))
                .setAllowedReadPrefixes(Arrays.asList(
                        translator.nonLocalized(METHOD_ALLOWED_READ_PREFIXES_DEFAULT).split(SPLIT_LIST_ENTRIES_REGEX)))
                .setAllowedWritePrefixes(Arrays.asList(
                        translator.nonLocalized(METHOD_ALLOWED_WRITE_PREFIXES_DEFAULT).split(SPLIT_LIST_ENTRIES_REGEX)))
                .build();
    }

    @Provides
    @Singleton
    ResourceConfiguration provideResourceConfiguration(final Translator translator) {
        return ResourceConfiguration.builder()
                .setMaxThreads(Integer.parseInt(translator.nonLocalized(MAX_THREADS_DEFAULT)))
                .build();
    }

    @Provides
    @Singleton
    RepositoryConfiguration provideRepositoryConfiguration(final Translator translator) {
        return RepositoryConfiguration.builder()
                .setRepositoryNameSuffix(translator.nonLocalized(REPOSITORY_NAME_SUFFIX_DEFAULT))
                .setRepositoryGenerateInterface(Boolean.parseBoolean(
                        translator.nonLocalized(REPOSITORY_GENERATE_INTERFACE_DEFAULT)))
                .build();
    }

    @Provides
    @Singleton
    LoggingConfiguration provideLoggingConfiguration(final Translator translator) {
        return LoggingConfiguration.builder()
                .setApi(LoggingApiOptions.valueOf(translator.nonLocalized(LOGGING_API_DEFAULT)))
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
                        translator.nonLocalized(DEFAULT_FLOW_STATE_CLASS_NAME_DEFAULT)))
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
                        translator.nonLocalized(DEFAULT_RESULT_STATE_CLASS_NAME_DEFAULT)))
                .setResultRowClass(ClassName.get(
                        names.basePackageName()
                                + "."
                                + names.utilityPackageName(),
                        translator.nonLocalized(DEFAULT_RESULT_ROW_CLASS_NAME_DEFAULT)))
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
