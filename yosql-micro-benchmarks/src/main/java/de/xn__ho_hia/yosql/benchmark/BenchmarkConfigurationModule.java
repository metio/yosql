package de.xn__ho_hia.yosql.benchmark;

import static de.xn__ho_hia.yosql.model.GenerateOptions.*;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Arrays;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.LoggingAPI;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import de.xn__ho_hia.yosql.model.Translator;

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
    @SuppressWarnings({ "nls" })
    ExecutionConfiguration provideExecutionConfiguration(final Translator messages) {
        final String basePackageName = messages.nonLocalized(BASE_PACKAGE_NAME_DEFAULT);
        final String utilityPackageName = messages.nonLocalized(UTILITY_PACKAGE_NAME_DEFAULT);
        final String converterPackageName = messages.nonLocalized(CONVERTER_PACKAGE_NAME_DEFAULT);
        final String defaultRowConverterAlias = messages.nonLocalized(DEFAULT_ROW_CONVERTER_DEFAULT);
        final String defaultResultRowClassName = messages.nonLocalized(DEFAULT_RESULT_ROW_CLASS_NAME_DEFAULT);

        final ResultRowConverter toResultRow = new ResultRowConverter();
        toResultRow.setAlias(defaultRowConverterAlias);
        toResultRow.setResultType(String.join(".", basePackageName, utilityPackageName, defaultResultRowClassName));
        toResultRow.setConverterType(String.join(".", basePackageName, converterPackageName,
                messages.nonLocalized(TO_RESULT_ROW_CONVERTER_CLASS_NAME)));

        return ExecutionConfiguration.builder()
                .setInputBaseDirectory(inputDirectory)
                .setOutputBaseDirectory(outputDirectory)
                .setBasePackageName(basePackageName)
                .setUtilityPackageName(utilityPackageName)
                .setConverterPackageName(converterPackageName)
                // TODO: offer several ways how SQL statements are embedded in generated repositories?
                // see https://github.com/sebhoss/yosql/issues/18
                .setRepositorySqlStatements("inline")
                .setGenerateStandardApi(Boolean.parseBoolean(messages.nonLocalized(METHOD_STANDARD_API_DEFAULT)))
                .setGenerateBatchApi(Boolean.parseBoolean(messages.nonLocalized(METHOD_BATCH_API_DEFAULT)))
                .setGenerateRxJavaApi(Boolean.parseBoolean(messages.nonLocalized(METHOD_RXJAVA_API_DEFAULT)))
                .setGenerateStreamEagerApi(Boolean.parseBoolean(messages.nonLocalized(METHOD_STREAM_EAGER_API_DEFAULT)))
                .setGenerateStreamLazyApi(Boolean.parseBoolean(messages.nonLocalized(METHOD_STREAM_LAZY_API_DEFAULT)))
                .setMethodBatchPrefix(messages.nonLocalized(METHOD_BATCH_PREFIX_DEFAULT))
                .setMethodBatchSuffix(messages.nonLocalized(METHOD_BATCH_SUFFIX_DEFAULT))
                .setMethodStreamPrefix(messages.nonLocalized(METHOD_STREAM_PREFIX_DEFAULT))
                .setMethodStreamSuffix(messages.nonLocalized(METHOD_STREAM_SUFFIX_DEFAULT))
                .setMethodRxJavaPrefix(messages.nonLocalized(METHOD_RXJAVA_PREFIX_DEFAULT))
                .setMethodRxJavaSuffix(messages.nonLocalized(METHOD_RXJAVA_SUFFIX_DEFAULT))
                .setMethodEagerName(messages.nonLocalized(METHOD_EAGER_NAME_DEFAULT))
                .setMethodLazyName(messages.nonLocalized(METHOD_LAZY_NAME_DEFAULT))
                .setRepositoryNameSuffix(messages.nonLocalized(REPOSITORY_NAME_SUFFIX_DEFAULT))
                .setSqlStatementSeparator(messages.nonLocalized(SQL_STATEMENT_SEPARATOR_DEFAULT))
                .setSqlFilesCharset(messages.nonLocalized(SQL_FILES_CHARSET_DEFAULT))
                .setAllowedCallPrefixes(
                        Arrays.asList(messages.nonLocalized(METHOD_ALLOWED_CALL_PREFIXES_DEFAULT).split(",")))
                .setAllowedReadPrefixes(
                        Arrays.asList(messages.nonLocalized(METHOD_ALLOWED_READ_PREFIXES_DEFAULT).split(",")))
                .setAllowedWritePrefixes(
                        Arrays.asList(messages.nonLocalized(METHOD_ALLOWED_WRITE_PREFIXES_DEFAULT).split(",")))
                .setValidateMethodNamePrefixes(
                        Boolean.parseBoolean(messages.nonLocalized(METHOD_VALIDATE_NAME_PREFIXES_DEFAULT)))
                .setMethodCatchAndRethrow(Boolean.parseBoolean(messages.nonLocalized(METHOD_CATCH_AND_RETHROW_DEFAULT)))
                .setClassGeneratedAnnotation(
                        Boolean.parseBoolean(messages.nonLocalized(GENERATED_ANNOTATION_CLASS_DEFAULT)))
                .setFieldGeneratedAnnotation(
                        Boolean.parseBoolean(messages.nonLocalized(GENERATED_ANNOTATION_FIELD_DEFAULT)))
                .setMethodGeneratedAnnotation(
                        Boolean.parseBoolean(messages.nonLocalized(GENERATED_ANNOTATION_METHOD_DEFAULT)))
                .setGeneratedAnnotationComment(messages.nonLocalized(GENERATED_ANNOTATION_COMMENT_DEFAULT))
                .setRepositoryGenerateInterface(
                        Boolean.parseBoolean(messages.nonLocalized(REPOSITORY_GENERATE_INTERFACE_DEFAULT)))
                .setLoggingApi(LoggingAPI.valueOf(messages.nonLocalized(LOGGING_API_DEFAULT)))
                .setDefaulFlowStateClassName(messages.nonLocalized(DEFAULT_FLOW_STATE_CLASS_NAME_DEFAULT))
                .setDefaultResultStateClassName(messages.nonLocalized(DEFAULT_RESULT_STATE_CLASS_NAME_DEFAULT))
                .setDefaultResultRowClassName(defaultResultRowClassName)
                .setDefaultRowConverter(defaultRowConverterAlias)
                .setResultRowConverters(Arrays.asList(toResultRow))
                .build();
    }

}
