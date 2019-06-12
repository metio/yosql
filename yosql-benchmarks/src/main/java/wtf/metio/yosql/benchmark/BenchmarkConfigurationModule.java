/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark;

import static wtf.metio.yosql.model.GenerateOptions.*;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Arrays;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.model.ExecutionConfiguration;
import wtf.metio.yosql.model.LoggingAPI;
import wtf.metio.yosql.model.ResultRowConverter;
import wtf.metio.yosql.model.Translator;

/**
 * Module for {@link PrintStream}.
 */
@Module
public class BenchmarkConfigurationModule {

    private final Path inputDirectory;
    private final Path outputDirectory;

    /**
     * @param inputDirectory
     *            The input directory to use.
     * @param outputDirectory
     *            The output directory to use.
     */
    public BenchmarkConfigurationModule(
            final Path inputDirectory,
            final Path outputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
    }

    @Provides
    ExecutionConfiguration provideExecutionConfiguration(final Translator translator) {
        final String basePackageName = translator.nonLocalized(BASE_PACKAGE_NAME_DEFAULT);
        final String utilityPackageName = translator.nonLocalized(UTILITY_PACKAGE_NAME_DEFAULT);
        final String converterPackageName = translator.nonLocalized(CONVERTER_PACKAGE_NAME_DEFAULT);
        final String defaultRowConverterAlias = translator.nonLocalized(DEFAULT_ROW_CONVERTER_DEFAULT);
        final String defaultResultRowClassName = translator.nonLocalized(DEFAULT_RESULT_ROW_CLASS_NAME_DEFAULT);

        final ResultRowConverter toResultRow = new ResultRowConverter();
        toResultRow.setAlias(defaultRowConverterAlias);
        toResultRow.setResultType(String.join(".", basePackageName, utilityPackageName, defaultResultRowClassName));
        toResultRow.setConverterType(String.join(".", basePackageName, converterPackageName,
                translator.nonLocalized(TO_RESULT_ROW_CONVERTER_CLASS_NAME)));

        return ExecutionConfiguration.builder()
                .setMaxThreads(Integer.parseInt(translator.nonLocalized(MAX_THREADS_DEFAULT)))
                .setInputBaseDirectory(inputDirectory)
                .setOutputBaseDirectory(outputDirectory)
                .setBasePackageName(basePackageName)
                .setUtilityPackageName(utilityPackageName)
                .setConverterPackageName(converterPackageName)
                // TODO: offer several ways how SQL statements are embedded in generated repositories?
                // see https://github.com/sebhoss/yosql/issues/18
                .setRepositorySqlStatements("inline")
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
                .setRepositoryNameSuffix(translator.nonLocalized(REPOSITORY_NAME_SUFFIX_DEFAULT))
                .setSqlStatementSeparator(translator.nonLocalized(SQL_STATEMENT_SEPARATOR_DEFAULT))
                .setSqlFilesCharset(translator.nonLocalized(SQL_FILES_CHARSET_DEFAULT))
                .setAllowedCallPrefixes(
                        Arrays.asList(translator.nonLocalized(METHOD_ALLOWED_CALL_PREFIXES_DEFAULT).split(",")))
                .setAllowedReadPrefixes(
                        Arrays.asList(translator.nonLocalized(METHOD_ALLOWED_READ_PREFIXES_DEFAULT).split(",")))
                .setAllowedWritePrefixes(
                        Arrays.asList(translator.nonLocalized(METHOD_ALLOWED_WRITE_PREFIXES_DEFAULT).split(",")))
                .setValidateMethodNamePrefixes(
                        Boolean.parseBoolean(translator.nonLocalized(METHOD_VALIDATE_NAME_PREFIXES_DEFAULT)))
                .setMethodCatchAndRethrow(Boolean.parseBoolean(translator.nonLocalized(METHOD_CATCH_AND_RETHROW_DEFAULT)))
                .setClassGeneratedAnnotation(
                        Boolean.parseBoolean(translator.nonLocalized(GENERATED_ANNOTATION_CLASS_DEFAULT)))
                .setFieldGeneratedAnnotation(
                        Boolean.parseBoolean(translator.nonLocalized(GENERATED_ANNOTATION_FIELD_DEFAULT)))
                .setMethodGeneratedAnnotation(
                        Boolean.parseBoolean(translator.nonLocalized(GENERATED_ANNOTATION_METHOD_DEFAULT)))
                .setGeneratedAnnotationComment(translator.nonLocalized(GENERATED_ANNOTATION_COMMENT_DEFAULT))
                .setRepositoryGenerateInterface(
                        Boolean.parseBoolean(translator.nonLocalized(REPOSITORY_GENERATE_INTERFACE_DEFAULT)))
                .setLoggingApi(LoggingAPI.valueOf(translator.nonLocalized(LOGGING_API_DEFAULT)))
                .setDefaulFlowStateClassName(translator.nonLocalized(DEFAULT_FLOW_STATE_CLASS_NAME_DEFAULT))
                .setDefaultResultStateClassName(translator.nonLocalized(DEFAULT_RESULT_STATE_CLASS_NAME_DEFAULT))
                .setDefaultResultRowClassName(defaultResultRowClassName)
                .setDefaultRowConverter(defaultRowConverterAlias)
                .setResultRowConverters(Arrays.asList(toResultRow))
                .build();
    }

}
