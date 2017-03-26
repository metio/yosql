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
        final Path inputBaseDirectory = args != null && args.length > 0 ? Paths.get(args[0]) : Paths.get(".");
        final Path outputBaseDirectory = args != null && args.length > 1 ? Paths.get(args[1]) : Paths.get(".");
        final String basePackageName = args != null && args.length > 2 ? args[2] : null;

        final Builder configurationBuilder = YoSql.prepareDefaultConfiguration();
        if (basePackageName != null && !basePackageName.isEmpty()) {
            configurationBuilder.setBasePackageName(basePackageName);
        }
        final ExecutionConfiguration configuration = configurationBuilder
                .setOutputBaseDirectory(outputBaseDirectory)
                .build();

        final ExecutionErrors errors = new ExecutionErrors();
        final SqlConfigurationFactory configurationFactory = new SqlConfigurationFactory(errors, configuration);
        final SqlFileParser sqlFileParser = new SqlFileParser(errors, configuration, configurationFactory);
        final SqlFileResolver<Path> fileResolver = new PathBasedSqlFileResolver();
        final TypeWriter typeWriter = new TypeWriter(errors);
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
        final YoSql<Path> yoSql = new YoSql<>(fileResolver, sqlFileParser, repositoryGenerator, utilsGenerator, errors,
                configuration);

        yoSql.generateFiles(inputBaseDirectory);
    }

}
