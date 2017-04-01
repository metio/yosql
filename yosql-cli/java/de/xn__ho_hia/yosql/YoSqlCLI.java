/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql;

import java.nio.file.Path;
import java.nio.file.Paths;

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
import de.xn__ho_hia.yosql.model.ExecutionConfiguration.Builder;
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.parser.PathBasedSqlFileResolver;
import de.xn__ho_hia.yosql.parser.SqlConfigurationFactory;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 *
 *
 */
public class YoSqlCLI {

    /**
     * @param args
     *            The CLI arguments.
     */
    @SuppressWarnings("nls")
    public static void main(final String[] args) {
        final OptionParser parser = new OptionParser();
        final OptionSpec<String> inputBaseDirectory = parser.accepts("inputBaseDirectory").withOptionalArg()
                .ofType(String.class).defaultsTo(".");
        final OptionSpec<String> outputBaseDirectory = parser.accepts("outputBaseDirectory").withOptionalArg()
                .ofType(String.class).defaultsTo(".");
        final OptionSpec<String> basePackageName = parser.accepts("basePackageName").withOptionalArg()
                .ofType(String.class).defaultsTo("com.example.persistence");
        parser.accepts("utilityPackageName");
        parser.accepts("converterPackageName");
        parser.accepts("java");
        parser.accepts("repositoryNameSuffix");
        parser.accepts("defaultRowConverter");
        parser.accepts("sqlFilesCharset");
        parser.accepts("sqlStatementSeparator");
        final OptionSet options = parser.parse(args);

        final Path input = Paths.get(options.valueOf(inputBaseDirectory));
        final Path output = Paths.get(options.valueOf(outputBaseDirectory));
        final String packageName = options.valueOf(basePackageName);

        System.out.println("Input directory: " + input);
        System.out.println("Output directory: " + output);

        final Builder configurationBuilder = YoSql.prepareDefaultConfiguration();
        if (packageName != null && !packageName.isEmpty()) {
            configurationBuilder.setBasePackageName(packageName);
        }
        final ExecutionConfiguration configuration = configurationBuilder
                .setOutputBaseDirectory(output)
                .build();

        final ExecutionErrors errors = new ExecutionErrors();
        final SqlConfigurationFactory configurationFactory = new SqlConfigurationFactory(errors, configuration);
        final SqlFileParser sqlFileParser = new SqlFileParser(errors, configuration, configurationFactory);
        final GeneratorPreconditions preconditions = new GeneratorPreconditions(errors);
        final SqlFileResolver<Path> fileResolver = new PathBasedSqlFileResolver(preconditions, errors);
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
        final YoSql<Path> yoSql = new YoSql<>(fileResolver, sqlFileParser, repositoryGenerator, utilsGenerator, errors);

        yoSql.generateFiles(input);
    }

}
