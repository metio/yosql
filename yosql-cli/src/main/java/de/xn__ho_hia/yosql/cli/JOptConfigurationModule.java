package de.xn__ho_hia.yosql.cli;

import static de.xn__ho_hia.yosql.model.ConfigurationOptions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import ch.qos.cal10n.IMessageConveyor;
import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.LoggingAPI;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConverter;

/**
 * Provides the default configuration.
 */
@Module
@SuppressWarnings("static-method")
public final class JOptConfigurationModule {

    private final String[] arguments;

    /**
     * @param arguments
     *            The CLI arguments to use.
     */
    public JOptConfigurationModule(final String[] arguments) {
        this.arguments = arguments;
    }

    @Provides
    ValueConverter<Path> providePathValueConverter(final ExecutionErrors errors) {
        return new PathValueConverter(errors);
    }

    @Provides
    @SuppressWarnings("nls")
    ExecutionConfiguration provideExecutionConfiguration(final ValueConverter<Path> pathConverter,
            final IMessageConveyor messages) {
        final OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        final Path currentDirectory = Paths.get(messages.getMessage(CURRENT_DIRECTORY));
        final OptionSpec<Path> inputBaseDirectory = parser
                .accepts(messages.getMessage(INPUT_BASE_DIRECTORY))
                .withRequiredArg()
                .withValuesConvertedBy(pathConverter)
                .defaultsTo(currentDirectory)
                .describedAs(messages.getMessage(INPUT_BASE_DIRECTORY_DESCRIPTION));
        final OptionSpec<Path> outputBaseDirectory = parser
                .accepts(messages.getMessage(OUTPUT_BASE_DIRECTORY))
                .withRequiredArg()
                .withValuesConvertedBy(pathConverter)
                .defaultsTo(currentDirectory)
                .describedAs(messages.getMessage(INPUT_BASE_DIRECTORY_DESCRIPTION));
        final OptionSpec<String> basePackageName = parser
                .accepts(messages.getMessage(BASE_PACKAGE_NAME))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(BASE_PACKAGE_NAME_DEFAULT))
                .describedAs(messages.getMessage(BASE_PACKAGE_NAME_DESCRIPTION));
        final OptionSpec<String> utilityPackageName = parser
                .accepts(messages.getMessage(UTILITY_PACKAGE_NAME))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(UTILITY_PACKAGE_NAME_DEFAULT))
                .describedAs(messages.getMessage(UTILITY_PACKAGE_NAME_DESCRIPTION));
        final OptionSpec<String> converterPackageName = parser
                .accepts(messages.getMessage(CONVERTER_PACKAGE_NAME))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(CONVERTER_PACKAGE_NAME_DEFAULT))
                .describedAs(messages.getMessage(CONVERTER_PACKAGE_NAME_DESCRIPTION));
        final OptionSpec<String> repositoryNameSuffix = parser
                .accepts(messages.getMessage(REPOSITORY_NAME_SUFFIX))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(REPOSITORY_NAME_SUFFIX_DEFAULT))
                .describedAs(messages.getMessage(REPOSITORY_NAME_SUFFIX_DESCRIPTION));
        final OptionSpec<String> sqlFilesCharset = parser
                .accepts(messages.getMessage(SQL_FILES_CHARSET))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(SQL_FILES_CHARSET_DEFAULT))
                .describedAs(messages.getMessage(SQL_FILES_CHARSET_DESCRIPTION));
        final OptionSpec<String> sqlStatementSeparator = parser
                .accepts(messages.getMessage(SQL_STATEMENT_SEPARATOR))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(SQL_STATEMENT_SEPARATOR_DEFAULT))
                .describedAs(messages.getMessage(SQL_STATEMENT_SEPARATOR_DESCRIPTION));
        final OptionSpec<String> defaultRowConverter = parser
                .accepts(messages.getMessage(DEFAULT_ROW_CONVERTER))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(DEFAULT_ROW_CONVERTER_DEFAULT))
                .describedAs(messages.getMessage(DEFAULT_ROW_CONVERTER_DESCRIPTION));
        final OptionSpec<String> methodBatchPrefix = parser
                .accepts(messages.getMessage(METHOD_BATCH_PREFIX))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(METHOD_BATCH_PREFIX_DEFAULT))
                .describedAs(messages.getMessage(METHOD_BATCH_PREFIX_DESCRIPTION));
        final OptionSpec<String> methodBatchSuffix = parser
                .accepts(messages.getMessage(METHOD_BATCH_SUFFIX))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(METHOD_BATCH_SUFFIX_DEFAULT))
                .describedAs(messages.getMessage(METHOD_BATCH_SUFFIX_DESCRIPTION));
        final OptionSpec<String> methodStreamPrefix = parser
                .accepts(messages.getMessage(METHOD_STREAM_PREFIX))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(METHOD_STREAM_PREFIX_DEFAULT))
                .describedAs(messages.getMessage(METHOD_STREAM_PREFIX_DESCRIPTION));
        final OptionSpec<String> methodStreamSuffix = parser
                .accepts(messages.getMessage(METHOD_STREAM_SUFFIX))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(METHOD_STREAM_SUFFIX_DEFAULT))
                .describedAs(messages.getMessage(METHOD_STREAM_SUFFIX_DESCRIPTION));
        final OptionSpec<String> methodRxJavaPrefix = parser
                .accepts(messages.getMessage(METHOD_RXJAVA_PREFIX))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(METHOD_RXJAVA_PREFIX_DEFAULT))
                .describedAs(messages.getMessage(METHOD_RXJAVA_PREFIX_DESCRIPTION));
        final OptionSpec<String> methodRxJavaSuffix = parser
                .accepts(messages.getMessage(METHOD_RXJAVA_SUFFIX))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(METHOD_RXJAVA_SUFFIX_DEFAULT))
                .describedAs(messages.getMessage(METHOD_RXJAVA_SUFFIX_DESCRIPTION));
        final OptionSpec<String> methodEagerName = parser
                .accepts(messages.getMessage(METHOD_EAGER_NAME))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(METHOD_EAGER_NAME_DEFAULT))
                .describedAs(messages.getMessage(METHOD_EAGER_NAME_DESCRIPTION));
        final OptionSpec<String> methodLazyName = parser
                .accepts(messages.getMessage(METHOD_LAZY_NAME))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(METHOD_LAZY_NAME_DEFAULT))
                .describedAs(messages.getMessage(METHOD_LAZY_NAME_DESCRIPTION));
        final OptionSpec<Boolean> generateStandardApi = parser
                .accepts(messages.getMessage(METHOD_STANDARD_API))
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(messages.getMessage(METHOD_STANDARD_API_DEFAULT)))
                .describedAs(messages.getMessage(METHOD_STANDARD_API_DESCRIPTION));
        final OptionSpec<Boolean> generateBatchApi = parser
                .accepts(messages.getMessage(METHOD_BATCH_API))
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(messages.getMessage(METHOD_BATCH_API_DEFAULT)))
                .describedAs(messages.getMessage(METHOD_BATCH_API_DESCRIPTION));
        final OptionSpec<Boolean> generateRxJavaApi = parser
                .accepts(messages.getMessage(METHOD_RXJAVA_API))
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(messages.getMessage(METHOD_RXJAVA_API_DEFAULT)))
                .describedAs(messages.getMessage(METHOD_RXJAVA_API_DESCRIPTION));
        final OptionSpec<Boolean> generateStreamEagerApi = parser
                .accepts(messages.getMessage(METHOD_STREAM_EAGER_API))
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(messages.getMessage(METHOD_STREAM_EAGER_API_DEFAULT)))
                .describedAs(messages.getMessage(METHOD_STREAM_EAGER_API_DESCRIPTION));
        final OptionSpec<Boolean> generateStreamLazyApi = parser
                .accepts(messages.getMessage(METHOD_STREAM_LAZY_API))
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(messages.getMessage(METHOD_STREAM_LAZY_API_DEFAULT)))
                .describedAs(messages.getMessage(METHOD_STREAM_LAZY_API_DESCRIPTION));
        final OptionSpec<String> allowedCallPrefixes = parser
                .accepts(messages.getMessage(METHOD_ALLOWED_CALL_PREFIXES))
                .withRequiredArg()
                .withValuesSeparatedBy(",")
                .ofType(String.class)
                .defaultsTo(messages.getMessage(METHOD_ALLOWED_CALL_PREFIXES_DEFAULT).split(","))
                .describedAs(messages.getMessage(METHOD_ALLOWED_CALL_PREFIXES_DESCRIPTION));
        final OptionSpec<String> allowedReadPrefixes = parser
                .accepts(messages.getMessage(METHOD_ALLOWED_READ_PREFIXES))
                .withRequiredArg()
                .withValuesSeparatedBy(",")
                .ofType(String.class)
                .defaultsTo(messages.getMessage(METHOD_ALLOWED_READ_PREFIXES_DEFAULT).split(","))
                .describedAs(messages.getMessage(METHOD_ALLOWED_READ_PREFIXES_DESCRIPTION));
        final OptionSpec<String> allowedWritePrefixes = parser
                .accepts(messages.getMessage(METHOD_ALLOWED_WRITE_PREFIXES))
                .withRequiredArg()
                .withValuesSeparatedBy(",")
                .ofType(String.class)
                .defaultsTo(messages.getMessage(METHOD_ALLOWED_WRITE_PREFIXES_DEFAULT).split(","))
                .describedAs(messages.getMessage(METHOD_ALLOWED_WRITE_PREFIXES_DESCRIPTION));
        final OptionSpec<Boolean> methodValidateNamePrefixes = parser
                .accepts(messages.getMessage(METHOD_VALIDATE_NAME_PREFIXES))
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(messages.getMessage(METHOD_VALIDATE_NAME_PREFIXES_DEFAULT)))
                .describedAs(messages.getMessage(METHOD_VALIDATE_NAME_PREFIXES_DESCRIPTION));
        final OptionSpec<Boolean> methodCatchAndRethrow = parser
                .accepts(messages.getMessage(METHOD_CATCH_AND_RETHROW))
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(messages.getMessage(METHOD_CATCH_AND_RETHROW_DEFAULT)))
                .describedAs(messages.getMessage(METHOD_CATCH_AND_RETHROW_DESCRIPTION));
        final OptionSpec<Boolean> classGeneratedAnnotation = parser
                .accepts(messages.getMessage(GENERATED_ANNOTATION_CLASS))
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(messages.getMessage(GENERATED_ANNOTATION_CLASS_DEFAULT)))
                .describedAs(messages.getMessage(GENERATED_ANNOTATION_CLASS_DESCRIPTION));
        final OptionSpec<Boolean> fieldGeneratedAnnotation = parser
                .accepts(messages.getMessage(GENERATED_ANNOTATION_FIELD))
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(messages.getMessage(GENERATED_ANNOTATION_FIELD_DEFAULT)))
                .describedAs(messages.getMessage(GENERATED_ANNOTATION_FIELD_DESCRIPTION));
        final OptionSpec<Boolean> methodGeneratedAnnotation = parser
                .accepts(messages.getMessage(GENERATED_ANNOTATION_METHOD))
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(messages.getMessage(GENERATED_ANNOTATION_METHOD_DEFAULT)))
                .describedAs(messages.getMessage(GENERATED_ANNOTATION_METHOD_DESCRIPTION));
        final OptionSpec<String> generatedAnnotationComment = parser
                .accepts(messages.getMessage(GENERATED_ANNOTATION_COMMENT))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(GENERATED_ANNOTATION_COMMENT_DEFAULT))
                .describedAs(messages.getMessage(GENERATED_ANNOTATION_COMMENT_DESCRIPTION));
        final OptionSpec<Boolean> repositoryGenerateInterface = parser
                .accepts(messages.getMessage(REPOSITORY_GENERATE_INTERFACE))
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(messages.getMessage(REPOSITORY_GENERATE_INTERFACE_DEFAULT)))
                .describedAs(messages.getMessage(REPOSITORY_GENERATE_INTERFACE_DESCRIPTION));
        final OptionSpec<String> defaulFlowStateClassName = parser
                .accepts(messages.getMessage(DEFAULT_FLOW_STATE_CLASS_NAME))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(DEFAULT_FLOW_STATE_CLASS_NAME_DEFAULT))
                .describedAs(messages.getMessage(DEFAULT_FLOW_STATE_CLASS_NAME_DESCRIPTION));
        final OptionSpec<String> defaultResultStateClassName = parser
                .accepts(messages.getMessage(DEFAULT_RESULT_STATE_CLASS_NAME))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(DEFAULT_RESULT_STATE_CLASS_NAME_DEFAULT))
                .describedAs(messages.getMessage(DEFAULT_RESULT_STATE_CLASS_NAME_DESCRIPTION));
        final OptionSpec<String> defaultResultRowClassName = parser
                .accepts(messages.getMessage(DEFAULT_RESULT_ROW_CLASS_NAME))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(DEFAULT_RESULT_ROW_CLASS_NAME_DEFAULT))
                .describedAs(messages.getMessage(DEFAULT_RESULT_ROW_CLASS_NAME_DESCRIPTION));
        final OptionSpec<LoggingAPI> loggingApi = parser
                .accepts(messages.getMessage(LOGGING_API))
                .withRequiredArg()
                .ofType(LoggingAPI.class)
                .defaultsTo(LoggingAPI.valueOf(messages.getMessage(LOGGING_API_DEFAULT)))
                .describedAs(messages.getMessage(LOGGING_API_DESCRIPTION));

        final OptionSet options = parser.parse(arguments);
        final ResultRowConverter toResultRow = new ResultRowConverter();
        toResultRow.setAlias(options.valueOf(defaultRowConverter));
        toResultRow.setResultType(
                String.join(".", options.valueOf(basePackageName), options.valueOf(utilityPackageName),
                        options.valueOf(defaultResultRowClassName)));
        toResultRow.setConverterType(
                String.join(".", options.valueOf(basePackageName), options.valueOf(converterPackageName),
                        messages.getMessage(TO_RESULT_ROW_CONVERTER_CLASS_NAME)));

        return ExecutionConfiguration.builder()
                .setInputBaseDirectory(options.valueOf(inputBaseDirectory))
                .setOutputBaseDirectory(options.valueOf(outputBaseDirectory))
                .setBasePackageName(options.valueOf(basePackageName))
                .setUtilityPackageName(options.valueOf(utilityPackageName))
                .setConverterPackageName(options.valueOf(converterPackageName))
                .setRepositoryNameSuffix(options.valueOf(repositoryNameSuffix))
                .setRepositorySqlStatements("inline")
                .setMethodBatchPrefix(options.valueOf(methodBatchPrefix))
                .setMethodBatchSuffix(options.valueOf(methodBatchSuffix))
                .setMethodStreamPrefix(options.valueOf(methodStreamPrefix))
                .setMethodStreamSuffix(options.valueOf(methodStreamSuffix))
                .setMethodRxJavaPrefix(options.valueOf(methodRxJavaPrefix))
                .setMethodRxJavaSuffix(options.valueOf(methodRxJavaSuffix))
                .setMethodEagerName(options.valueOf(methodEagerName))
                .setMethodLazyName(options.valueOf(methodLazyName))
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
                .setResultRowConverters(Arrays.asList(toResultRow))
                .build();
    }

}
