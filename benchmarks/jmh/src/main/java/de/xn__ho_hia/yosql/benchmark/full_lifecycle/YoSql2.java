/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.benchmark.full_lifecycle;

import static de.xn__ho_hia.yosql.i18n.ConfigurationOptions.*;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.groupingBy;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.inject.Inject;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import de.xn__ho_hia.yosql.YoSql;
import de.xn__ho_hia.yosql.generator.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.UtilitiesGenerator;
import de.xn__ho_hia.yosql.model.CodeGenerationException;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration.Builder;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.LoggingAPI;
import de.xn__ho_hia.yosql.model.ResultRowConverter;
import de.xn__ho_hia.yosql.model.SqlStatement;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import de.xn__ho_hia.yosql.utils.Timer;

/**
 * Alternative implementation of {@link YoSql} used for benchmarking.
 */
public class YoSql2 implements YoSql {

    private final SqlFileResolver     fileResolver;
    private final SqlFileParser       sqlFileParser;
    private final RepositoryGenerator repositoryGenerator;
    private final UtilitiesGenerator  utilsGenerator;
    private final ExecutionErrors     errors;
    private final Timer               timer;

    /**
     * @param fileResolver
     *            The file resolver to use.
     * @param sqlFileParser
     *            The SQL file parser to use.
     * @param repositoryGenerator
     *            The repository generator to use.
     * @param utilsGenerator
     *            The utilities generator to use.
     * @param errors
     *            The error collector to use.
     * @param timer
     *            The timer to use.
     */
    @Inject
    public YoSql2(
            final SqlFileResolver fileResolver,
            final SqlFileParser sqlFileParser,
            final RepositoryGenerator repositoryGenerator,
            final UtilitiesGenerator utilsGenerator,
            final ExecutionErrors errors,
            final Timer timer) {
        this.fileResolver = fileResolver;
        this.sqlFileParser = sqlFileParser;
        this.repositoryGenerator = repositoryGenerator;
        this.utilsGenerator = utilsGenerator;
        this.errors = errors;
        this.timer = timer;
    }

    /**
     * @return A configuration builder initialized with all default values.
     */
    @SuppressWarnings({ "nls" })
    public static Builder defaultConfiguration() {
        final IMessageConveyor messages = new MessageConveyor(Locale.ENGLISH);

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
                .setResultRowConverters(Arrays.asList(toResultRow));
    }

    /**
     */
    @Override
    @SuppressWarnings("nls")
    public void generateFiles() {
        final Executor executor = Executors.newCachedThreadPool();
        supplyAsync(() -> timer.timed("parse statements",
                () -> fileResolver.resolveFiles()
                        .flatMap(sqlFileParser::parse)
                        .collect(Collectors.toList())),
                executor)
                        .thenApplyAsync(this::generateRepositories)
                        .thenAcceptAsync(this::generateUtilities)
                        .join();
        if (errors.hasErrors()) {
            errors.throwWith(new CodeGenerationException("Error during code generation"));
        }
    }

    private void generateUtilities(final List<SqlStatement> statements) {
        timer.timed("generate utilities", () -> utilsGenerator.generateUtilities(statements)); //$NON-NLS-1$
    }

    private List<SqlStatement> generateRepositories(final List<SqlStatement> statements) {
        timer.timed("generate repositories", () -> statements.stream() //$NON-NLS-1$
                .collect(groupingBy(SqlStatement::getRepository))
                .entrySet()
                .parallelStream()
                .forEach(repository -> repositoryGenerator.generateRepository(repository.getKey(),
                        repository.getValue())));
        return statements;
    }

}
