/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql;

import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.BASE_PACKAGE_NAME;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.BASE_PACKAGE_NAME_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.BASE_PACKAGE_NAME_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.CONVERTER_PACKAGE_NAME;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.CONVERTER_PACKAGE_NAME_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.CONVERTER_PACKAGE_NAME_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.CURRENT_DIRECTORY;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.DEFAULT_ROW_CONVERTER;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.DEFAULT_ROW_CONVERTER_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.DEFAULT_ROW_CONVERTER_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.GENERATE_STANDARD_API;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.GENERATE_STANDARD_API_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.GENERATE_STANDARD_API_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.INPUT_BASE_DIRECTORY;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.INPUT_BASE_DIRECTORY_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.JAVA;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.JAVA_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.JAVA_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_BATCH_PREFIX;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_BATCH_PREFIX_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_BATCH_PREFIX_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_BATCH_SUFFIX;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_BATCH_SUFFIX_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_BATCH_SUFFIX_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_EAGER_NAME;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_EAGER_NAME_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_EAGER_NAME_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_LAZY_NAME;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_LAZY_NAME_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_LAZY_NAME_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_RXJAVA_PREFIX;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_RXJAVA_PREFIX_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_RXJAVA_PREFIX_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_RXJAVA_SUFFIX;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_RXJAVA_SUFFIX_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_RXJAVA_SUFFIX_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_STREAM_PREFIX;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_STREAM_PREFIX_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_STREAM_PREFIX_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_STREAM_SUFFIX;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_STREAM_SUFFIX_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.METHOD_STREAM_SUFFIX_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.OUTPUT_BASE_DIRECTORY;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.REPOSITORY_NAME_SUFFIX;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.REPOSITORY_NAME_SUFFIX_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.REPOSITORY_NAME_SUFFIX_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.SQL_FILES_CHARSET;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.SQL_FILES_CHARSET_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.SQL_FILES_CHARSET_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.SQL_STATEMENT_SEPARATOR;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.SQL_STATEMENT_SEPARATOR_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.SQL_STATEMENT_SEPARATOR_DESCRIPTION;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.UTILITY_PACKAGE_NAME;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.UTILITY_PACKAGE_NAME_DEFAULT;
import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.UTILITY_PACKAGE_NAME_DESCRIPTION;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import de.xn__ho_hia.yosql.cli.PathValueConverter;
import de.xn__ho_hia.yosql.generator.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.GeneratorPreconditions;
import de.xn__ho_hia.yosql.generator.LoggingGenerator;
import de.xn__ho_hia.yosql.generator.TypeWriter;
import de.xn__ho_hia.yosql.generator.helpers.TypicalCodeBlocks;
import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;
import de.xn__ho_hia.yosql.generator.logging.NoOpLoggingGenerator;
import de.xn__ho_hia.yosql.generator.raw_jdbc.RawJdbcBatchMethodGenerator;
import de.xn__ho_hia.yosql.generator.raw_jdbc.RawJdbcJava8StreamMethodGenerator;
import de.xn__ho_hia.yosql.generator.raw_jdbc.RawJdbcMethodGenerator;
import de.xn__ho_hia.yosql.generator.raw_jdbc.RawJdbcRepositoryFieldGenerator;
import de.xn__ho_hia.yosql.generator.raw_jdbc.RawJdbcRepositoryGenerator;
import de.xn__ho_hia.yosql.generator.raw_jdbc.RawJdbcRxJavaMethodGenerator;
import de.xn__ho_hia.yosql.generator.raw_jdbc.RawJdbcStandardMethodGenerator;
import de.xn__ho_hia.yosql.generator.utils.DefaultUtilitiesGenerator;
import de.xn__ho_hia.yosql.generator.utils.FlowStateGenerator;
import de.xn__ho_hia.yosql.generator.utils.ResultRowGenerator;
import de.xn__ho_hia.yosql.generator.utils.ResultStateGenerator;
import de.xn__ho_hia.yosql.generator.utils.ToResultRowConverterGenerator;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.LoggingAPI;
import de.xn__ho_hia.yosql.parser.PathBasedSqlFileResolver;
import de.xn__ho_hia.yosql.parser.SqlConfigurationFactory;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConverter;

/**
 * Command line interface for YoSQL.
 */
public class YoSqlCLI {

    /**
     * @param args
     *            The CLI arguments.
     */
    public static void main(final String[] args) {
        final IMessageConveyor messages = new MessageConveyor(Locale.ENGLISH);
        final ExecutionErrors errors = new ExecutionErrors();
        final ExecutionConfiguration configuration = createConfiguration(args, messages, errors);
        final YoSql yoSql = createYoSql(configuration, errors);

        yoSql.generateFiles();
    }

    @SuppressWarnings({ "nls" })
    private static ExecutionConfiguration createConfiguration(
            final String[] args,
            final IMessageConveyor messages,
            final ExecutionErrors errors) {
        final ValueConverter<Path> pathConverter = new PathValueConverter(errors);
        final OptionParser parser = new OptionParser();
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
        final OptionSpec<String> java = parser
                .accepts(messages.getMessage(JAVA))
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(messages.getMessage(JAVA_DEFAULT))
                .describedAs(messages.getMessage(JAVA_DESCRIPTION));
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
                .accepts(messages.getMessage(GENERATE_STANDARD_API))
                .withRequiredArg()
                .ofType(Boolean.class)
                .defaultsTo(Boolean.valueOf(messages.getMessage(GENERATE_STANDARD_API_DEFAULT)))
                .describedAs(messages.getMessage(GENERATE_STANDARD_API_DESCRIPTION));

        final OptionSet options = parser.parse(args);
        final int javaVersion = parseJavaVersion(options.valueOf(java));

        return YoSql.prepareDefaultConfiguration()
                .setInputBaseDirectory(options.valueOf(inputBaseDirectory))
                .setOutputBaseDirectory(options.valueOf(outputBaseDirectory))
                .setBasePackageName(options.valueOf(basePackageName))
                .setUtilityPackageName(options.valueOf(utilityPackageName))
                .setConverterPackageName(options.valueOf(converterPackageName))
                .setRepositoryNameSuffix(options.valueOf(repositoryNameSuffix))
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
                .setGenerateBatchApi(true)
                .setGenerateStreamEagerApi(javaVersion >= 8)
                .setGenerateStreamLazyApi(javaVersion >= 8)
                .setGenerateRxJavaApi(true)
                .setAllowedCallPrefixes(Arrays.asList("call", "execute"))
                .setAllowedReadPrefixes(Arrays.asList("select", "read", "query", "find"))
                .setAllowedWritePrefixes(Arrays.asList("update", "insert", "delete", "create", "write", "add", "remove",
                        "merge", "drop"))
                .setValidateMethodNamePrefixes(true)
                .setMethodCatchAndRethrow(true)
                .setClassGeneratedAnnotation(true)
                .setFieldGeneratedAnnotation(true)
                .setMethodGeneratedAnnotation(true)
                .setRepositoryGenerateInterface(true)
                .setGeneratedAnnotationComment("DO NOT EDIT")
                .setLoggingApi(LoggingAPI.JDK)
                .setDefaulFlowStateClassName(FlowStateGenerator.FLOW_STATE_CLASS_NAME)
                .setDefaultResultStateClassName(ResultStateGenerator.RESULT_STATE_CLASS_NAME)
                .setDefaultResultRowClassName(ResultRowGenerator.RESULT_ROW_CLASS_NAME)
                .setDefaultRowConverter(options.valueOf(defaultRowConverter))
                .build();
    }

    private static int parseJavaVersion(final String givenJavaVersion) {
        final String[] versionParts = givenJavaVersion.split("\\."); //$NON-NLS-1$
        String versionToParse = versionParts[0];
        if (versionParts.length > 1) {
            versionToParse = versionParts[1];
        }
        final int javaVersion = Integer.parseInt(versionToParse);
        return javaVersion;
    }

    private static YoSql createYoSql(final ExecutionConfiguration configuration, final ExecutionErrors errors) {
        final SqlConfigurationFactory configurationFactory = new SqlConfigurationFactory(errors, configuration);
        final SqlFileParser sqlFileParser = new SqlFileParser(errors, configuration, configurationFactory);
        final GeneratorPreconditions preconditions = new GeneratorPreconditions(errors);
        final SqlFileResolver fileResolver = new PathBasedSqlFileResolver(preconditions, errors, configuration);
        final TypeWriter typeWriter = new TypeWriter(errors, System.out);
        final AnnotationGenerator annotations = new AnnotationGenerator(configuration);
        final LoggingGenerator logging = new NoOpLoggingGenerator();
        final TypicalCodeBlocks codeBlocks = new TypicalCodeBlocks(configuration, logging);
        final RawJdbcRxJavaMethodGenerator rxJavaMethodGenerator = new RawJdbcRxJavaMethodGenerator(configuration,
                codeBlocks, annotations);
        final RawJdbcJava8StreamMethodGenerator java8StreamMethodGenerator = new RawJdbcJava8StreamMethodGenerator(
                codeBlocks, annotations);
        final RawJdbcBatchMethodGenerator batchMethodGenerator = new RawJdbcBatchMethodGenerator(annotations,
                codeBlocks);
        final RawJdbcStandardMethodGenerator standardMethodGenerator = new RawJdbcStandardMethodGenerator(codeBlocks,
                annotations);
        final RawJdbcMethodGenerator methodGenerator = new RawJdbcMethodGenerator(rxJavaMethodGenerator,
                java8StreamMethodGenerator, batchMethodGenerator, standardMethodGenerator, annotations);
        final TypicalFields fields = new TypicalFields(annotations);
        final RawJdbcRepositoryFieldGenerator fieldGenerator = new RawJdbcRepositoryFieldGenerator(fields, logging);
        final RawJdbcRepositoryGenerator repositoryGenerator = new RawJdbcRepositoryGenerator(typeWriter, configuration,
                annotations, methodGenerator, fieldGenerator);
        final FlowStateGenerator flowStateGenerator = new FlowStateGenerator(annotations, typeWriter, configuration);
        final ResultStateGenerator resultStateGenerator = new ResultStateGenerator(annotations, typeWriter,
                configuration);
        final ToResultRowConverterGenerator toResultRowConverterGenerator = new ToResultRowConverterGenerator(
                annotations, typeWriter, configuration);
        final ResultRowGenerator resultRowGenerator = new ResultRowGenerator(annotations, typeWriter, configuration);
        final DefaultUtilitiesGenerator utilsGenerator = new DefaultUtilitiesGenerator(flowStateGenerator,
                resultStateGenerator, toResultRowConverterGenerator, resultRowGenerator);

        return new YoSql(fileResolver, sqlFileParser, repositoryGenerator, utilsGenerator, errors);
    }

}
