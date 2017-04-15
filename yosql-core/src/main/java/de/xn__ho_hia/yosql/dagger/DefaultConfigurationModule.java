package de.xn__ho_hia.yosql.dagger;

import static de.xn__ho_hia.yosql.model.ConfigurationOptions.*;

import java.nio.file.Paths;
import java.util.Arrays;

import ch.qos.cal10n.IMessageConveyor;
import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.LoggingAPI;
import de.xn__ho_hia.yosql.model.ResultRowConverter;

/**
 * Provides the default configuration.
 */
@Module
@SuppressWarnings("static-method")
public final class DefaultConfigurationModule {

    @Provides
    @SuppressWarnings("nls")
    ExecutionConfiguration provideExecutionConfiguration(final IMessageConveyor messages) {
        final String basePackageName = messages.getMessage(BASE_PACKAGE_NAME_DEFAULT);
        final String utilityPackageName = messages.getMessage(UTILITY_PACKAGE_NAME_DEFAULT);
        final String converterPackageName = messages.getMessage(CONVERTER_PACKAGE_NAME_DEFAULT);
        final String defaultRowConverterAlias = messages.getMessage(DEFAULT_ROW_CONVERTER_DEFAULT);
        final String defaultResultRowClassName = messages.getMessage(DEFAULT_RESULT_ROW_CLASS_NAME_DEFAULT);

        final ResultRowConverter toResultRow = new ResultRowConverter();
        toResultRow.setAlias(defaultRowConverterAlias);
        toResultRow.setResultType(String.join(".", basePackageName, utilityPackageName, defaultResultRowClassName));
        toResultRow.setConverterType(String.join(".", basePackageName, converterPackageName,
                messages.getMessage(TO_RESULT_ROW_CONVERTER_CLASS_NAME)));

        return ExecutionConfiguration.builder()
                .setInputBaseDirectory(Paths.get(messages.getMessage(CURRENT_DIRECTORY)))
                .setOutputBaseDirectory(Paths.get(messages.getMessage(CURRENT_DIRECTORY)))
                .setBasePackageName(basePackageName)
                .setUtilityPackageName(utilityPackageName)
                .setConverterPackageName(converterPackageName)
                // TODO: offer several ways how SQL statements are embedded in generated repositories?
                // see https://github.com/sebhoss/yosql/issues/18
                .setRepositorySqlStatements("inline")
                .setGenerateStandardApi(Boolean.parseBoolean(messages.getMessage(METHOD_STANDARD_API_DEFAULT)))
                .setGenerateBatchApi(Boolean.parseBoolean(messages.getMessage(METHOD_BATCH_API_DEFAULT)))
                .setGenerateRxJavaApi(Boolean.parseBoolean(messages.getMessage(METHOD_RXJAVA_API_DEFAULT)))
                .setGenerateStreamEagerApi(Boolean.parseBoolean(messages.getMessage(METHOD_STREAM_EAGER_API_DEFAULT)))
                .setGenerateStreamLazyApi(Boolean.parseBoolean(messages.getMessage(METHOD_STREAM_LAZY_API_DEFAULT)))
                .setMethodBatchPrefix(messages.getMessage(METHOD_BATCH_PREFIX_DEFAULT))
                .setMethodBatchSuffix(messages.getMessage(METHOD_BATCH_SUFFIX_DEFAULT))
                .setMethodStreamPrefix(messages.getMessage(METHOD_STREAM_PREFIX_DEFAULT))
                .setMethodStreamSuffix(messages.getMessage(METHOD_STREAM_SUFFIX_DEFAULT))
                .setMethodRxJavaPrefix(messages.getMessage(METHOD_RXJAVA_PREFIX_DEFAULT))
                .setMethodRxJavaSuffix(messages.getMessage(METHOD_RXJAVA_SUFFIX_DEFAULT))
                .setMethodEagerName(messages.getMessage(METHOD_EAGER_NAME_DEFAULT))
                .setMethodLazyName(messages.getMessage(METHOD_LAZY_NAME_DEFAULT))
                .setRepositoryNameSuffix(messages.getMessage(REPOSITORY_NAME_SUFFIX_DEFAULT))
                .setSqlStatementSeparator(messages.getMessage(SQL_STATEMENT_SEPARATOR_DEFAULT))
                .setSqlFilesCharset(messages.getMessage(SQL_FILES_CHARSET_DEFAULT))
                .setAllowedCallPrefixes(
                        Arrays.asList(messages.getMessage(METHOD_ALLOWED_CALL_PREFIXES_DEFAULT).split(",")))
                .setAllowedReadPrefixes(
                        Arrays.asList(messages.getMessage(METHOD_ALLOWED_READ_PREFIXES_DEFAULT).split(",")))
                .setAllowedWritePrefixes(
                        Arrays.asList(messages.getMessage(METHOD_ALLOWED_WRITE_PREFIXES_DEFAULT).split(",")))
                .setValidateMethodNamePrefixes(
                        Boolean.parseBoolean(messages.getMessage(METHOD_VALIDATE_NAME_PREFIXES_DEFAULT)))
                .setMethodCatchAndRethrow(Boolean.parseBoolean(messages.getMessage(METHOD_CATCH_AND_RETHROW_DEFAULT)))
                .setClassGeneratedAnnotation(
                        Boolean.parseBoolean(messages.getMessage(GENERATED_ANNOTATION_CLASS_DEFAULT)))
                .setFieldGeneratedAnnotation(
                        Boolean.parseBoolean(messages.getMessage(GENERATED_ANNOTATION_FIELD_DEFAULT)))
                .setMethodGeneratedAnnotation(
                        Boolean.parseBoolean(messages.getMessage(GENERATED_ANNOTATION_METHOD_DEFAULT)))
                .setGeneratedAnnotationComment(messages.getMessage(GENERATED_ANNOTATION_COMMENT_DEFAULT))
                .setRepositoryGenerateInterface(
                        Boolean.parseBoolean(messages.getMessage(REPOSITORY_GENERATE_INTERFACE_DEFAULT)))
                .setLoggingApi(LoggingAPI.valueOf(messages.getMessage(LOGGING_API_DEFAULT)))
                .setDefaulFlowStateClassName(messages.getMessage(DEFAULT_FLOW_STATE_CLASS_NAME_DEFAULT))
                .setDefaultResultStateClassName(messages.getMessage(DEFAULT_RESULT_STATE_CLASS_NAME_DEFAULT))
                .setDefaultResultRowClassName(defaultResultRowClassName)
                .setDefaultRowConverter(defaultRowConverterAlias)
                .setResultRowConverters(Arrays.asList(toResultRow))
                .build();
    }

}
