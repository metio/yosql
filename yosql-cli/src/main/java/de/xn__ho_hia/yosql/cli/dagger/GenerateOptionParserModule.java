/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli.dagger;

import static de.xn__ho_hia.yosql.cli.i18n.Commands.GENERATE;
import static de.xn__ho_hia.yosql.model.GenerateOptionDescriptions.*;
import static de.xn__ho_hia.yosql.model.GenerateOptions.*;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.cli.i18n.Commands;
import de.xn__ho_hia.yosql.cli.parser.YoSqlOptionParser;
import de.xn__ho_hia.yosql.model.LoggingAPI;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import de.xn__ho_hia.yosql.model.Translator;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.HelpFormatter;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConverter;

@Module
@SuppressWarnings("static-method")
class GenerateOptionParserModule extends AbstractOptionParserModule {

    private static final String COMMA = ","; //$NON-NLS-1$

    @Provides
    @Singleton
    @UsedFor.Command(GENERATE)
    OptionParser provideParser(final HelpFormatter helpFormatter) {
        return createParser(helpFormatter);
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(LOG_LEVEL)
    OptionSpec<String> provideLogLevelOption(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return createLogLevelOption(parser, translator);
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(MAX_THREADS)
    OptionSpec<Integer> provideMaxThreadsOption(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return integerOption(parser, translator.nonLocalized(MAX_THREADS))
                .defaultsTo(Integer.valueOf(translator.nonLocalized(MAX_THREADS_DEFAULT)))
                .describedAs(translator.localized(MAX_THREADS_DESCRIPTION));
    }

    private ArgumentAcceptingOptionSpec<Integer> integerOption(final OptionParser parser, final String optionName) {
        return parser.accepts(optionName)
                .withRequiredArg()
                .ofType(Integer.class);
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(INPUT_BASE_DIRECTORY)
    OptionSpec<Path> provideInputOption(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator,
            final ValueConverter<Path> pathConverter) {
        final Path currentDirectory = Paths.get(translator.nonLocalized(CURRENT_DIRECTORY));
        return parser.accepts(translator.nonLocalized(INPUT_BASE_DIRECTORY))
                .withRequiredArg()
                .withValuesConvertedBy(pathConverter)
                .defaultsTo(currentDirectory)
                .describedAs(translator.localized(INPUT_BASE_DIRECTORY_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(OUTPUT_BASE_DIRECTORY)
    OptionSpec<Path> provideOutputBaseDirectoryOption(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator,
            final ValueConverter<Path> pathConverter) {
        final Path currentDirectory = Paths.get(translator.nonLocalized(CURRENT_DIRECTORY));
        return parser.accepts(translator.nonLocalized(OUTPUT_BASE_DIRECTORY))
                .withRequiredArg()
                .withValuesConvertedBy(pathConverter)
                .defaultsTo(currentDirectory)
                .describedAs(translator.localized(OUTPUT_BASE_DIRECTORY_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(BASE_PACKAGE_NAME)
    OptionSpec<String> provideBasePackageNameOption(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(BASE_PACKAGE_NAME))
                .defaultsTo(translator.nonLocalized(BASE_PACKAGE_NAME_DEFAULT))
                .describedAs(translator.localized(BASE_PACKAGE_NAME_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(UTILITY_PACKAGE_NAME)
    OptionSpec<String> provideUtilityPackageNameOption(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(UTILITY_PACKAGE_NAME))
                .defaultsTo(translator.nonLocalized(UTILITY_PACKAGE_NAME_DEFAULT))
                .describedAs(translator.localized(UTILITY_PACKAGE_NAME_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(CONVERTER_PACKAGE_NAME)
    OptionSpec<String> provideConverterPackageNameOption(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(CONVERTER_PACKAGE_NAME))
                .defaultsTo(translator.nonLocalized(CONVERTER_PACKAGE_NAME_DEFAULT))
                .describedAs(translator.localized(CONVERTER_PACKAGE_NAME_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(REPOSITORY_NAME_SUFFIX)
    OptionSpec<String> provideRepositoryNameSuffixOption(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(REPOSITORY_NAME_SUFFIX))
                .defaultsTo(translator.nonLocalized(REPOSITORY_NAME_SUFFIX_DEFAULT))
                .describedAs(translator.localized(REPOSITORY_NAME_SUFFIX_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(SQL_FILES_CHARSET)
    OptionSpec<String> sqlFilesCharset(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(SQL_FILES_CHARSET))
                .defaultsTo(translator.nonLocalized(SQL_FILES_CHARSET_DEFAULT))
                .describedAs(translator.localized(SQL_FILES_CHARSET_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(SQL_STATEMENT_SEPARATOR)
    OptionSpec<String> sqlStatementSeparator(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(SQL_STATEMENT_SEPARATOR))
                .defaultsTo(translator.nonLocalized(SQL_STATEMENT_SEPARATOR_DEFAULT))
                .describedAs(translator.localized(SQL_STATEMENT_SEPARATOR_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(DEFAULT_ROW_CONVERTER)
    OptionSpec<String> defaultRowConverter(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(DEFAULT_ROW_CONVERTER))
                .defaultsTo(translator.nonLocalized(DEFAULT_ROW_CONVERTER_DEFAULT))
                .describedAs(translator.localized(DEFAULT_ROW_CONVERTER_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_BATCH_PREFIX)
    OptionSpec<String> methodBatchPrefix(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(METHOD_BATCH_PREFIX))
                .defaultsTo(translator.nonLocalized(METHOD_BATCH_PREFIX_DEFAULT))
                .describedAs(translator.localized(METHOD_BATCH_PREFIX_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_BATCH_SUFFIX)
    OptionSpec<String> methodBatchSuffix(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(METHOD_BATCH_SUFFIX))
                .defaultsTo(translator.nonLocalized(METHOD_BATCH_SUFFIX_DEFAULT))
                .describedAs(translator.localized(METHOD_BATCH_SUFFIX_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_STREAM_PREFIX)
    OptionSpec<String> methodStreamPrefix(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(METHOD_STREAM_PREFIX))
                .defaultsTo(translator.nonLocalized(METHOD_STREAM_PREFIX_DEFAULT))
                .describedAs(translator.localized(METHOD_STREAM_PREFIX_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_STREAM_SUFFIX)
    OptionSpec<String> methodStreamSuffix(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(METHOD_STREAM_SUFFIX))
                .defaultsTo(translator.nonLocalized(METHOD_STREAM_SUFFIX_DEFAULT))
                .describedAs(translator.localized(METHOD_STREAM_SUFFIX_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_RXJAVA_PREFIX)
    OptionSpec<String> methodRxJavaPrefix(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(METHOD_RXJAVA_PREFIX))
                .defaultsTo(translator.nonLocalized(METHOD_RXJAVA_PREFIX_DEFAULT))
                .describedAs(translator.localized(METHOD_RXJAVA_PREFIX_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_RXJAVA_SUFFIX)
    OptionSpec<String> methodRxJavaSuffix(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(METHOD_RXJAVA_SUFFIX))
                .defaultsTo(translator.nonLocalized(METHOD_RXJAVA_SUFFIX_DEFAULT))
                .describedAs(translator.localized(METHOD_RXJAVA_SUFFIX_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_EAGER_NAME)
    OptionSpec<String> methodEagerName(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(METHOD_EAGER_NAME))
                .defaultsTo(translator.nonLocalized(METHOD_EAGER_NAME_DEFAULT))
                .describedAs(translator.localized(METHOD_EAGER_NAME_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_LAZY_NAME)
    OptionSpec<String> methodLazyName(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(METHOD_LAZY_NAME))
                .defaultsTo(translator.nonLocalized(METHOD_LAZY_NAME_DEFAULT))
                .describedAs(translator.localized(METHOD_LAZY_NAME_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_STANDARD_API)
    OptionSpec<Boolean> generateStandardApi(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return booleanOption(parser,
                translator.nonLocalized(METHOD_STANDARD_API),
                translator.nonLocalized(METHOD_STANDARD_API_DEFAULT))
                        .describedAs(translator.localized(METHOD_STANDARD_API_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_BATCH_API)
    OptionSpec<Boolean> generateBatchApi(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return booleanOption(parser,
                translator.nonLocalized(METHOD_BATCH_API),
                translator.nonLocalized(METHOD_BATCH_API_DEFAULT))
                        .describedAs(translator.localized(METHOD_BATCH_API_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_RXJAVA_API)
    OptionSpec<Boolean> generateRxJavaApi(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return booleanOption(parser,
                translator.nonLocalized(METHOD_RXJAVA_API),
                translator.nonLocalized(METHOD_RXJAVA_API_DEFAULT))
                        .describedAs(translator.localized(METHOD_RXJAVA_API_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_STREAM_EAGER_API)
    OptionSpec<Boolean> generateStreamEagerApi(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return booleanOption(parser,
                translator.nonLocalized(METHOD_STREAM_EAGER_API),
                translator.nonLocalized(METHOD_STREAM_EAGER_API_DEFAULT))
                        .describedAs(translator.localized(METHOD_STREAM_EAGER_API_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_STREAM_LAZY_API)
    OptionSpec<Boolean> generateStreamLazyApi(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return booleanOption(parser,
                translator.nonLocalized(METHOD_STREAM_LAZY_API),
                translator.nonLocalized(METHOD_STREAM_LAZY_API_DEFAULT))
                        .describedAs(translator.localized(METHOD_STREAM_LAZY_API_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_ALLOWED_CALL_PREFIXES)
    OptionSpec<String> allowedCallPrefixes(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(METHOD_ALLOWED_CALL_PREFIXES))
                .withValuesSeparatedBy(COMMA)
                .defaultsTo(translator.nonLocalized(METHOD_ALLOWED_CALL_PREFIXES_DEFAULT).split(COMMA))
                .describedAs(translator.localized(METHOD_ALLOWED_CALL_PREFIXES_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_ALLOWED_READ_PREFIXES)
    OptionSpec<String> allowedReadPrefixes(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(METHOD_ALLOWED_READ_PREFIXES))
                .withValuesSeparatedBy(COMMA)
                .defaultsTo(translator.nonLocalized(METHOD_ALLOWED_READ_PREFIXES_DEFAULT).split(COMMA))
                .describedAs(translator.localized(METHOD_ALLOWED_READ_PREFIXES_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_ALLOWED_WRITE_PREFIXES)
    OptionSpec<String> allowedWritePrefixes(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(METHOD_ALLOWED_WRITE_PREFIXES))
                .withValuesSeparatedBy(COMMA)
                .defaultsTo(translator.nonLocalized(METHOD_ALLOWED_WRITE_PREFIXES_DEFAULT).split(COMMA))
                .describedAs(translator.localized(METHOD_ALLOWED_WRITE_PREFIXES_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_VALIDATE_NAME_PREFIXES)
    OptionSpec<Boolean> methodValidateNamePrefixes(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return booleanOption(parser,
                translator.nonLocalized(METHOD_VALIDATE_NAME_PREFIXES),
                translator.nonLocalized(METHOD_VALIDATE_NAME_PREFIXES_DEFAULT))
                        .describedAs(translator.localized(METHOD_VALIDATE_NAME_PREFIXES_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(METHOD_CATCH_AND_RETHROW)
    OptionSpec<Boolean> methodCatchAndRethrow(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return booleanOption(parser,
                translator.nonLocalized(METHOD_CATCH_AND_RETHROW),
                translator.nonLocalized(METHOD_CATCH_AND_RETHROW_DEFAULT))
                        .describedAs(translator.localized(METHOD_CATCH_AND_RETHROW_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(GENERATED_ANNOTATION_CLASS)
    OptionSpec<Boolean> classGeneratedAnnotation(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return booleanOption(parser,
                translator.nonLocalized(GENERATED_ANNOTATION_CLASS),
                translator.nonLocalized(GENERATED_ANNOTATION_CLASS_DEFAULT))
                        .describedAs(translator.localized(GENERATED_ANNOTATION_CLASS_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(GENERATED_ANNOTATION_FIELD)
    OptionSpec<Boolean> fieldGeneratedAnnotation(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return booleanOption(parser,
                translator.nonLocalized(GENERATED_ANNOTATION_FIELD),
                translator.nonLocalized(GENERATED_ANNOTATION_FIELD_DEFAULT))
                        .describedAs(translator.localized(GENERATED_ANNOTATION_FIELD_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(GENERATED_ANNOTATION_METHOD)
    OptionSpec<Boolean> methodGeneratedAnnotation(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return booleanOption(parser,
                translator.nonLocalized(GENERATED_ANNOTATION_METHOD),
                translator.nonLocalized(GENERATED_ANNOTATION_METHOD_DEFAULT))
                        .describedAs(translator.localized(GENERATED_ANNOTATION_METHOD_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(REPOSITORY_GENERATE_INTERFACE)
    OptionSpec<Boolean> repositoryGenerateInterface(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return booleanOption(parser,
                translator.nonLocalized(REPOSITORY_GENERATE_INTERFACE),
                translator.nonLocalized(REPOSITORY_GENERATE_INTERFACE_DEFAULT))
                        .describedAs(translator.localized(REPOSITORY_GENERATE_INTERFACE_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(GENERATED_ANNOTATION_COMMENT)
    OptionSpec<String> generatedAnnotationComment(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(GENERATED_ANNOTATION_COMMENT))
                .defaultsTo(translator.nonLocalized(GENERATED_ANNOTATION_COMMENT_DEFAULT))
                .describedAs(translator.localized(GENERATED_ANNOTATION_COMMENT_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(DEFAULT_FLOW_STATE_CLASS_NAME)
    OptionSpec<String> defaulFlowStateClassName(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(DEFAULT_FLOW_STATE_CLASS_NAME))
                .defaultsTo(translator.nonLocalized(DEFAULT_FLOW_STATE_CLASS_NAME_DEFAULT))
                .describedAs(translator.localized(DEFAULT_FLOW_STATE_CLASS_NAME_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(DEFAULT_RESULT_STATE_CLASS_NAME)
    OptionSpec<String> defaultResultStateClassName(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(DEFAULT_RESULT_STATE_CLASS_NAME))
                .defaultsTo(translator.nonLocalized(DEFAULT_RESULT_STATE_CLASS_NAME_DEFAULT))
                .describedAs(translator.localized(DEFAULT_RESULT_STATE_CLASS_NAME_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(DEFAULT_RESULT_ROW_CLASS_NAME)
    OptionSpec<String> defaultResultRowClassName(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(DEFAULT_RESULT_ROW_CLASS_NAME))
                .defaultsTo(translator.nonLocalized(DEFAULT_RESULT_ROW_CLASS_NAME_DEFAULT))
                .describedAs(translator.localized(DEFAULT_RESULT_ROW_CLASS_NAME_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(SQL_FILES_SUFFIX)
    OptionSpec<String> sqlFilesSuffix(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return stringOption(parser, translator.nonLocalized(SQL_FILES_SUFFIX))
                .defaultsTo(translator.nonLocalized(SQL_FILES_SUFFIX_DEFAULT))
                .describedAs(translator.localized(SQL_FILES_SUFFIX_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(LOGGING_API)
    OptionSpec<LoggingAPI> loggingApi(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator) {
        return parser.accepts(translator.nonLocalized(LOGGING_API))
                .withRequiredArg()
                .ofType(LoggingAPI.class)
                .defaultsTo(LoggingAPI.valueOf(translator.nonLocalized(LOGGING_API_DEFAULT)))
                .describedAs(translator.localized(LOGGING_API_DESCRIPTION));
    }

    @Provides
    @Singleton
    @UsedFor.GenerateOption(RESULT_ROW_CONVERTERS)
    OptionSpec<ResultRowConverter> resultRowConverters(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            final Translator translator,
            final ValueConverter<ResultRowConverter> converterConverter) {
        return parser.accepts(translator.nonLocalized(RESULT_ROW_CONVERTERS))
                .withOptionalArg()
                .withValuesSeparatedBy(COMMA)
                .withValuesConvertedBy(converterConverter)
                .describedAs(translator.localized(RESULT_ROW_CONVERTERS_DESCRIPTION));
    }

    private ArgumentAcceptingOptionSpec<String> stringOption(final OptionParser parser, final String optionName) {
        return parser.accepts(optionName)
                .withRequiredArg()
                .ofType(String.class);
    }

    private ArgumentAcceptingOptionSpec<Boolean> booleanOption(
            final OptionParser parser,
            final String optionName,
            final String defaultValue) {
        return parser.accepts(optionName)
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(defaultValue));
    }

    @Provides
    @Singleton
    @UsedFor.Command(GENERATE)
    OptionSet provideGenerateOptionSet(
            @UsedFor.Command(GENERATE) final YoSqlOptionParser parser,
            @UsedFor.CLI final String[] arguments) {
        return parser.parse(arguments);
    }

    @Provides
    @Singleton
    Logger provideRootLogger(
            @UsedFor.Command(Commands.GENERATE) final OptionSet options,
            @UsedFor.GenerateOption(LOG_LEVEL) final OptionSpec<String> logLevel) {
        final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.valueOf(options.valueOf(logLevel).toUpperCase()));
        return root;
    }

    @Provides
    @Singleton
    @UsedFor.Command(GENERATE)
    YoSqlOptionParser provideYoSqlOptionParser(
            @UsedFor.Command(GENERATE) final OptionParser parser,
            @UsedFor.GenerateOption(LOG_LEVEL) final OptionSpec<String> logLevel,
            @UsedFor.GenerateOption(MAX_THREADS) final OptionSpec<Integer> maxThreads,
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
            @UsedFor.GenerateOption(RESULT_ROW_CONVERTERS) final OptionSpec<ResultRowConverter> resultRowConverters) {
        return new YoSqlOptionParser(parser, logLevel, inputBaseDirectory, outputBaseDirectory,
                basePackageName, maxThreads,
                utilityPackageName, converterPackageName, repositoryNameSuffix, sqlFilesCharset, defaultRowConverter,
                sqlStatementSeparator, methodBatchPrefix, methodBatchSuffix, methodStreamPrefix, methodStreamSuffix,
                methodRxJavaPrefix, methodRxJavaSuffix, methodEagerName, methodLazyName, generateStandardApi,
                generateBatchApi, generateRxJavaApi, generateStreamEagerApi, generateStreamLazyApi, allowedCallPrefixes,
                allowedReadPrefixes, allowedWritePrefixes, methodValidateNamePrefixes, methodCatchAndRethrow,
                classGeneratedAnnotation, fieldGeneratedAnnotation, methodGeneratedAnnotation,
                repositoryGenerateInterface, generatedAnnotationComment, defaulFlowStateClassName,
                defaultResultStateClassName, defaultResultRowClassName, sqlFilesSuffix, loggingApi,
                resultRowConverters);
    }

}
