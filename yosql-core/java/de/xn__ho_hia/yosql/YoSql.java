/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql;

import static java.util.stream.Collectors.groupingBy;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import de.xn__ho_hia.yosql.generator.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.UtilitiesGenerator;
import de.xn__ho_hia.yosql.generator.utils.FlowStateGenerator;
import de.xn__ho_hia.yosql.generator.utils.ResultRowGenerator;
import de.xn__ho_hia.yosql.generator.utils.ResultStateGenerator;
import de.xn__ho_hia.yosql.generator.utils.ToResultRowConverterGenerator;
import de.xn__ho_hia.yosql.model.CodeGenerationException;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration.Builder;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.LoggingAPI;
import de.xn__ho_hia.yosql.model.SqlStatement;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import de.xn__ho_hia.yosql.utils.Timer;

/**
 * @param <SOURCE>
 *            The type of the input source.
 */
public class YoSql<SOURCE> {

    private final SqlFileResolver<SOURCE> fileResolver;
    private final SqlFileParser           sqlFileParser;
    private final RepositoryGenerator     repositoryGenerator;
    private final UtilitiesGenerator      utilsGenerator;
    private final ExecutionErrors         errors;

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
     * @param configuration
     *            The configuration to use.
     */
    @Inject
    public YoSql(
            final SqlFileResolver<SOURCE> fileResolver,
            final SqlFileParser sqlFileParser,
            final RepositoryGenerator repositoryGenerator,
            final UtilitiesGenerator utilsGenerator,
            final ExecutionErrors errors,
            final ExecutionConfiguration configuration) {
        this.fileResolver = fileResolver;
        this.sqlFileParser = sqlFileParser;
        this.repositoryGenerator = repositoryGenerator;
        this.utilsGenerator = utilsGenerator;
        this.errors = errors;
    }

    /**
     * @return A configuration builder initialized with all default values.
     */
    @SuppressWarnings({ "nls" })
    public static Builder prepareDefaultConfiguration() {
        return ExecutionConfiguration.builder()
                .setOutputBaseDirectory(Paths.get("."))
                .setBasePackageName("com.example.persistence")
                .setRepositorySqlStatements("inline")
                .setGenerateStandardApi(true)
                .setGenerateBatchApi(true)
                .setGenerateRxJavaApi(true)
                .setGenerateStreamEagerApi(true)
                .setGenerateStreamLazyApi(true)
                .setMethodBatchPrefix("")
                .setMethodBatchSuffix("Batch")
                .setMethodStreamPrefix("")
                .setMethodStreamSuffix("Stream")
                .setMethodRxJavaPrefix("")
                .setMethodRxJavaSuffix("Flow")
                .setMethodEagerName("Eager")
                .setMethodLazyName("Lazy")
                .setRepositoryNameSuffix("Repository")
                .setUtilityPackageName("util")
                .setConverterPackageName("converter")
                .setSqlStatementSeparator(";")
                .setSqlFilesCharset("UTF-8")
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
                .setDefaultRowConverter(ToResultRowConverterGenerator.RESULT_ROW_CONVERTER_ALIAS)
                .setDefaulFlowStateClassName(FlowStateGenerator.FLOW_STATE_CLASS_NAME)
                .setDefaultResultStateClassName(ResultStateGenerator.RESULT_STATE_CLASS_NAME)
                .setDefaultResultRowClassName(ResultRowGenerator.RESULT_ROW_CLASS_NAME);
    }

    /**
     * @param inputBaseDirectory
     *            The base directory that contains all SQL files.
     */
    @SuppressWarnings("nls")
    public void generateFiles(final SOURCE inputBaseDirectory) {
        final List<SqlStatement> allStatements = Timer.timed("parse statements",
                () -> fileResolver.resolveFiles(inputBaseDirectory)
                        .flatMap(sqlFileParser::parse)
                        .sorted(Comparator.comparing(SqlStatement::getName))
                        .collect(Collectors.toList()));
        Timer.timed("generate repositories", () -> allStatements.stream()
                .collect(groupingBy(SqlStatement::getRepository))
                .forEach(repositoryGenerator::generateRepository));
        Timer.timed("generate utilities", () -> utilsGenerator.generateUtilities(allStatements));

        if (errors.hasErrors()) {
            errors.throwWith(new CodeGenerationException("Error during code generation"));
        }
    }

}
