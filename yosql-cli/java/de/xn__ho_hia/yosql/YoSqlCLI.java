/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql;

import java.nio.file.Path;

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
import de.xn__ho_hia.yosql.parser.PathBasedSqlFileResolver;
import de.xn__ho_hia.yosql.parser.SqlConfigurationFactory;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.ValueConverter;

/**
 *
 *
 */
public class YoSqlCLI {

    /**
     * @param args
     *            The CLI arguments.
     */
    public static void main(final String[] args) {
        final ExecutionConfiguration configuration = createConfiguration(args);
        final YoSql yoSql = createYoSql(configuration);

        yoSql.generateFiles();
    }

    @SuppressWarnings("nls")
    private static ExecutionConfiguration createConfiguration(final String[] args) {
        final ValueConverter<Path> pathConverter = new PathValueConverter();
        final OptionParser parser = new OptionParser();
        final OptionSpec<Path> inputBaseDirectory = parser.accepts("inputBaseDirectory")
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(".")
                .withValuesConvertedBy(pathConverter)
                .describedAs("The input directory for .sql files");
        final OptionSpec<Path> outputBaseDirectory = parser.accepts("outputBaseDirectory")
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo(".")
                .withValuesConvertedBy(pathConverter)
                .describedAs("The output directory for all generated files");
        final OptionSpec<String> basePackageName = parser.accepts("basePackageName")
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo("com.example.persistence")
                .describedAs("The base package name for the generated code.");
        final OptionSpec<String> utilityPackageName = parser.accepts("utilityPackageName")
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo("util")
                .describedAs("The package name suffix for the generated utilities.");
        final OptionSpec<String> converterPackageName = parser.accepts("converterPackageName")
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo("converter")
                .describedAs("The package name suffix for the generated converters.");
        final OptionSpec<String> java = parser.accepts("java")
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo("1.8")
                .describedAs("The target Java source version for the generated code.");
        final OptionSpec<String> repositoryNameSuffix = parser.accepts("repositoryNameSuffix")
                .withRequiredArg()
                .ofType(String.class)
                .defaultsTo("Repository")
                .describedAs("The repository name suffix to use for all generated repositories.");
        final OptionSpec<String> sqlFilesCharset = parser.accepts("sqlFilesCharset")
                .withRequiredArg()
                .ofType(String.class)
                .describedAs("The character set to use while reading .sql files.");
        final OptionSpec<String> sqlStatementSeparator = parser.accepts("sqlStatementSeparator")
                .withRequiredArg()
                .ofType(String.class)
                .describedAs(
                        "The SQL statement separator to use to split multiple SQL statement inside a single .sql file.");
        parser.accepts("defaultRowConverter");

        final OptionSet options = parser.parse(args);
        final int javaVersion = Integer.parseInt(options.valueOf(java));

        return YoSql.prepareDefaultConfiguration()
                .setInputBaseDirectory(options.valueOf(inputBaseDirectory))
                .setOutputBaseDirectory(options.valueOf(outputBaseDirectory))
                .setBasePackageName(options.valueOf(basePackageName))
                .setUtilityPackageName(options.valueOf(utilityPackageName))
                .setConverterPackageName(options.valueOf(converterPackageName))
                .setRepositoryNameSuffix(options.valueOf(repositoryNameSuffix))
                .setSqlFilesCharset(options.valueOf(sqlFilesCharset))
                .setSqlStatementSeparator(options.valueOf(sqlStatementSeparator))
                .setGenerateStreamEagerApi(javaVersion >= 8)
                .setGenerateStreamLazyApi(javaVersion >= 8)
                .build();
    }

    private static YoSql createYoSql(final ExecutionConfiguration configuration) {
        final ExecutionErrors errors = new ExecutionErrors();
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
