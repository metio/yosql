package de.xn__ho_hia.yosql;

import static java.util.stream.Collectors.groupingBy;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
import de.xn__ho_hia.yosql.model.ExecutionErrors;
import de.xn__ho_hia.yosql.model.SqlStatement;
import de.xn__ho_hia.yosql.parser.PathBasedSqlFileResolver;
import de.xn__ho_hia.yosql.parser.SqlConfigurationFactory;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import de.xn__ho_hia.yosql.utils.Timer;

public class YoSqlCLI {

    public static void main(String[] args) {
        final Path baseDirectory = args != null && args.length > 0 ? Paths.get(args[0]) : Paths.get(".");
        final ExecutionErrors errors = new ExecutionErrors();
        final ExecutionConfiguration configuration = new ExecutionConfiguration();
        final SqlConfigurationFactory configurationFactory = new SqlConfigurationFactory(errors, configuration);
        final SqlFileParser sqlFileParser = new SqlFileParser(errors, configuration, configurationFactory);
        final SqlFileResolver<Path> fileResolver = new PathBasedSqlFileResolver();
        final TypeWriter typeWriter = new TypeWriter(errors);
        final AnnotationGenerator annotations = new AnnotationGenerator(configuration);
        final LoggingGenerator logging = new NoOpLoggingGenerator();
        final TypicalCodeBlocks codeBlocks = new TypicalCodeBlocks(configuration, logging);
        final RawJdbcRxJavaMethodGenerator rxJavaMethodGenerator = new RawJdbcRxJavaMethodGenerator(configuration, codeBlocks, annotations);
        final RawJdbcJava8StreamMethodGenerator java8StreamMethodGenerator = new RawJdbcJava8StreamMethodGenerator(codeBlocks, annotations);
        final RawJdbcBatchMethodGenerator batchMethodGenerator = new RawJdbcBatchMethodGenerator(annotations, codeBlocks);
        final RawJdbcStandardMethodGenerator standardMethodGenerator = new RawJdbcStandardMethodGenerator(codeBlocks, annotations);
        final RawJdbcMethodGenerator methodGenerator = new RawJdbcMethodGenerator(rxJavaMethodGenerator, java8StreamMethodGenerator, batchMethodGenerator, standardMethodGenerator, annotations);
        final TypicalFields fields = new TypicalFields(annotations);
        final RawJdbcRepositoryFieldGenerator fieldGenerator = new RawJdbcRepositoryFieldGenerator(fields, logging);
        final RawJdbcRepositoryGenerator codeGenerator = new RawJdbcRepositoryGenerator(typeWriter, configuration, annotations, methodGenerator, fieldGenerator);
        final FlowStateGenerator flowStateGenerator = new FlowStateGenerator(annotations, typeWriter, configuration);
        final ResultStateGenerator resultStateGenerator = new ResultStateGenerator(annotations, typeWriter, configuration);
        final ToResultRowConverterGenerator toResultRowConverterGenerator = new ToResultRowConverterGenerator(annotations, typeWriter, configuration);
        final ResultRowGenerator resultRowGenerator = new ResultRowGenerator(annotations, typeWriter, configuration);
        final DefaultUtilitiesGenerator utilsGenerator = new DefaultUtilitiesGenerator(flowStateGenerator, resultStateGenerator, toResultRowConverterGenerator, resultRowGenerator);

        final List<SqlStatement> allStatements = Timer.timed("parse statements",
                () -> fileResolver.resolveFiles(baseDirectory)
                        .flatMap(sqlFileParser::parse)
                        .sorted(Comparator.comparing(SqlStatement::getName))
                        .collect(Collectors.toList()));
        Timer.timed("generate repositories", () -> allStatements.stream()
                .collect(groupingBy(SqlStatement::getRepository))
                .forEach(codeGenerator::generateRepository));
        Timer.timed("generate utilities", () -> utilsGenerator.generateUtilities(allStatements));
    }

}
