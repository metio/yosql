/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli;

import static de.xn__ho_hia.yosql.model.GenerateOptions.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.LoggingAPI;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import de.xn__ho_hia.yosql.model.Translator;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConverter;

/**
 * Provides the default configuration.
 */
@Module
@SuppressWarnings("static-method")
public final class JOptConfigurationModule {

    private static final String DOT = "."; //$NON-NLS-1$

    @Provides
    ValueConverter<Path> providePathValueConverter(final ExecutionErrors errors) {
        return new PathValueConverter(errors);
    }

    @Provides
    ValueConverter<ResultRowConverter> provideResultRowConverterConverter() {
        return new ResultRowConverterValueConverter();
    }

    @Provides
    ExecutionConfiguration provideExecutionConfiguration(
            @UsedFor.Command(Commands.GENERATE) final OptionSet options,
            @UsedFor.GenerateOption(INPUT_BASE_DIRECTORY) final OptionSpec<Path> inputBaseDirectory,
            @UsedFor.GenerateOption(OUTPUT_BASE_DIRECTORY) final OptionSpec<Path> outputBaseDirectory,
            @UsedFor.GenerateOption(BASE_PACKAGE_NAME) final OptionSpec<String> basePackageName,
            @UsedFor.GenerateOption(UTILITY_PACKAGE_NAME) final OptionSpec<String> utilityPackageName,
            @UsedFor.GenerateOption(CONVERTER_PACKAGE_NAME) final OptionSpec<String> converterPackageName,
            @UsedFor.GenerateOption(REPOSITORY_NAME_SUFFIX) final OptionSpec<String> repositoryNameSuffix,
            @UsedFor.GenerateOption(SQL_FILES_CHARSET) final OptionSpec<String> sqlFilesCharset,
            @UsedFor.GenerateOption(SQL_STATEMENT_SEPARATOR) final OptionSpec<String> sqlStatementSeparator,
            @UsedFor.GenerateOption(DEFAULT_ROW_CONVERTER) final OptionSpec<String> defaultRowConverter,
            @UsedFor.GenerateOption(METHOD_BATCH_PREFIX) final OptionSpec<String> methodBatchPrefix,
            @UsedFor.GenerateOption(METHOD_BATCH_SUFFIX) final OptionSpec<String> methodBatchSuffix,
            @UsedFor.GenerateOption(METHOD_STREAM_PREFIX) final OptionSpec<String> methodStreamPrefix,
            @UsedFor.GenerateOption(METHOD_STREAM_SUFFIX) final OptionSpec<String> methodStreamSuffix,
            @UsedFor.GenerateOption(METHOD_RXJAVA_PREFIX) final OptionSpec<String> methodRxJavaPrefix,
            @UsedFor.GenerateOption(METHOD_RXJAVA_SUFFIX) final OptionSpec<String> methodRxJavaSuffix,
            @UsedFor.GenerateOption(METHOD_EAGER_NAME) final OptionSpec<String> methodEagerName,
            @UsedFor.GenerateOption(METHOD_LAZY_NAME) final OptionSpec<String> methodLazyName,
            @UsedFor.GenerateOption(METHOD_STANDARD_API) final OptionSpec<Boolean> generateStandardApi,
            @UsedFor.GenerateOption(METHOD_BATCH_API) final OptionSpec<Boolean> generateBatchApi,
            @UsedFor.GenerateOption(METHOD_RXJAVA_API) final OptionSpec<Boolean> generateRxJavaApi,
            @UsedFor.GenerateOption(METHOD_STREAM_EAGER_API) final OptionSpec<Boolean> generateStreamEagerApi,
            @UsedFor.GenerateOption(METHOD_STREAM_LAZY_API) final OptionSpec<Boolean> generateStreamLazyApi,
            @UsedFor.GenerateOption(METHOD_ALLOWED_CALL_PREFIXES) final OptionSpec<String> allowedCallPrefixes,
            @UsedFor.GenerateOption(METHOD_ALLOWED_READ_PREFIXES) final OptionSpec<String> allowedReadPrefixes,
            @UsedFor.GenerateOption(METHOD_ALLOWED_WRITE_PREFIXES) final OptionSpec<String> allowedWritePrefixes,
            @UsedFor.GenerateOption(METHOD_VALIDATE_NAME_PREFIXES) final OptionSpec<Boolean> methodValidateNamePrefixes,
            @UsedFor.GenerateOption(METHOD_CATCH_AND_RETHROW) final OptionSpec<Boolean> methodCatchAndRethrow,
            @UsedFor.GenerateOption(GENERATED_ANNOTATION_CLASS) final OptionSpec<Boolean> classGeneratedAnnotation,
            @UsedFor.GenerateOption(GENERATED_ANNOTATION_FIELD) final OptionSpec<Boolean> fieldGeneratedAnnotation,
            @UsedFor.GenerateOption(GENERATED_ANNOTATION_METHOD) final OptionSpec<Boolean> methodGeneratedAnnotation,
            @UsedFor.GenerateOption(REPOSITORY_GENERATE_INTERFACE) final OptionSpec<Boolean> repositoryGenerateInterface,
            @UsedFor.GenerateOption(GENERATED_ANNOTATION_COMMENT) final OptionSpec<String> generatedAnnotationComment,
            @UsedFor.GenerateOption(DEFAULT_FLOW_STATE_CLASS_NAME) final OptionSpec<String> defaulFlowStateClassName,
            @UsedFor.GenerateOption(DEFAULT_RESULT_STATE_CLASS_NAME) final OptionSpec<String> defaultResultStateClassName,
            @UsedFor.GenerateOption(DEFAULT_RESULT_ROW_CLASS_NAME) final OptionSpec<String> defaultResultRowClassName,
            @UsedFor.GenerateOption(SQL_FILES_SUFFIX) final OptionSpec<String> sqlFilesSuffix,
            @UsedFor.GenerateOption(LOGGING_API) final OptionSpec<LoggingAPI> loggingApi,
            final List<ResultRowConverter> resultConverters) {
        return ExecutionConfiguration.builder()
                .setMaxThreads(0)
                .setInputBaseDirectory(options.valueOf(inputBaseDirectory))
                .setOutputBaseDirectory(options.valueOf(outputBaseDirectory))
                .setBasePackageName(options.valueOf(basePackageName))
                .setUtilityPackageName(options.valueOf(utilityPackageName))
                .setConverterPackageName(options.valueOf(converterPackageName))
                .setRepositoryNameSuffix(options.valueOf(repositoryNameSuffix))
                .setRepositorySqlStatements("inline") //$NON-NLS-1$
                .setMethodBatchPrefix(options.valueOf(methodBatchPrefix))
                .setMethodBatchSuffix(options.valueOf(methodBatchSuffix))
                .setMethodStreamPrefix(options.valueOf(methodStreamPrefix))
                .setMethodStreamSuffix(options.valueOf(methodStreamSuffix))
                .setMethodRxJavaPrefix(options.valueOf(methodRxJavaPrefix))
                .setMethodRxJavaSuffix(options.valueOf(methodRxJavaSuffix))
                .setMethodEagerName(options.valueOf(methodEagerName))
                .setMethodLazyName(options.valueOf(methodLazyName))
                .setSqlFilesSuffix(options.valueOf(sqlFilesSuffix))
                .setSqlFilesCharset(options.valueOf(sqlFilesCharset))
                .setSqlStatementSeparator(options.valueOf(sqlStatementSeparator))
                .setGenerateStandardApi(options.valueOf(generateStandardApi).booleanValue())
                .setGenerateBatchApi(options.valueOf(generateBatchApi).booleanValue())
                .setGenerateStreamEagerApi(options.valueOf(generateStreamEagerApi).booleanValue())
                .setGenerateStreamLazyApi(options.valueOf(generateStreamLazyApi).booleanValue())
                .setGenerateRxJavaApi(options.valueOf(generateRxJavaApi).booleanValue())
                .setAllowedCallPrefixes(options.valuesOf(allowedCallPrefixes))
                .setAllowedReadPrefixes(options.valuesOf(allowedReadPrefixes))
                .setAllowedWritePrefixes(options.valuesOf(allowedWritePrefixes))
                .setValidateMethodNamePrefixes(options.valueOf(methodValidateNamePrefixes).booleanValue())
                .setMethodCatchAndRethrow(options.valueOf(methodCatchAndRethrow).booleanValue())
                .setClassGeneratedAnnotation(options.valueOf(classGeneratedAnnotation).booleanValue())
                .setFieldGeneratedAnnotation(options.valueOf(fieldGeneratedAnnotation).booleanValue())
                .setMethodGeneratedAnnotation(options.valueOf(methodGeneratedAnnotation).booleanValue())
                .setGeneratedAnnotationComment(options.valueOf(generatedAnnotationComment))
                .setRepositoryGenerateInterface(options.valueOf(repositoryGenerateInterface).booleanValue())
                .setLoggingApi(options.valueOf(loggingApi))
                .setDefaulFlowStateClassName(options.valueOf(defaulFlowStateClassName))
                .setDefaultResultStateClassName(options.valueOf(defaultResultStateClassName))
                .setDefaultResultRowClassName(options.valueOf(defaultResultRowClassName))
                .setDefaultRowConverter(options.valueOf(defaultRowConverter))
                .setResultRowConverters(resultConverters)
                .build();
    }

    @Provides
    List<ResultRowConverter> provideResultConverters(
            final Translator messages,
            @UsedFor.Command(Commands.GENERATE) final OptionSet options,
            @UsedFor.GenerateOption(BASE_PACKAGE_NAME) final OptionSpec<String> basePackageName,
            @UsedFor.GenerateOption(UTILITY_PACKAGE_NAME) final OptionSpec<String> utilityPackageName,
            @UsedFor.GenerateOption(CONVERTER_PACKAGE_NAME) final OptionSpec<String> converterPackageName,
            @UsedFor.GenerateOption(DEFAULT_ROW_CONVERTER) final OptionSpec<String> defaultRowConverter,
            @UsedFor.GenerateOption(DEFAULT_RESULT_ROW_CLASS_NAME) final OptionSpec<String> defaultResultRowClassName,
            @UsedFor.GenerateOption(RESULT_ROW_CONVERTERS) final OptionSpec<ResultRowConverter> resultRowConverters) {
        final List<ResultRowConverter> resultConverters = new ArrayList<>();
        final ResultRowConverter toResultRow = new ResultRowConverter();
        toResultRow.setAlias(options.valueOf(defaultRowConverter));
        toResultRow.setResultType(
                String.join(DOT, options.valueOf(basePackageName), options.valueOf(utilityPackageName),
                        options.valueOf(defaultResultRowClassName)));
        toResultRow.setConverterType(
                String.join(DOT, options.valueOf(basePackageName), options.valueOf(converterPackageName),
                        messages.nonLocalized(TO_RESULT_ROW_CONVERTER_CLASS_NAME)));
        resultConverters.add(toResultRow);
        resultConverters.addAll(options.valuesOf(resultRowConverters));
        return resultConverters;
    }

}
