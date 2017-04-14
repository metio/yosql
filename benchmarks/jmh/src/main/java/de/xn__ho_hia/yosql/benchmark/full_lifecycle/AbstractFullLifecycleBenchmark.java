package de.xn__ho_hia.yosql.benchmark.full_lifecycle;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import de.xn__ho_hia.yosql.YoSql;
import de.xn__ho_hia.yosql.benchmark.AbstractBenchmark;
import de.xn__ho_hia.yosql.generator.AnnotationGenerator;
import de.xn__ho_hia.yosql.generator.LoggingGenerator;
import de.xn__ho_hia.yosql.generator.TypeWriter;
import de.xn__ho_hia.yosql.generator.helpers.TypicalCodeBlocks;
import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;
import de.xn__ho_hia.yosql.generator.logging.JdkLoggingGenerator;
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
import de.xn__ho_hia.yosql.parser.Java8SqlFileParser;
import de.xn__ho_hia.yosql.parser.ParserPreconditions;
import de.xn__ho_hia.yosql.parser.PathBasedSqlFileResolver;
import de.xn__ho_hia.yosql.parser.SqlConfigurationFactory;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import de.xn__ho_hia.yosql.utils.Timer;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
abstract class AbstractFullLifecycleBenchmark extends AbstractBenchmark {

    private YoSql yosql;

    @Setup
    public final void setUpYoSql() {
        final ExecutionConfiguration configuration = YoSql.defaultConfiguration()
                .setInputBaseDirectory(inputDirectory)
                .setOutputBaseDirectory(outputDirectory)
                .build();
        final ExecutionErrors errors = new ExecutionErrors();
        yosql = createYoSql(configuration, errors);
    }

    /**
     * Benchmarks code generation.
     *
     * @throws Exception
     *             In case anything goes wrong while generating files.
     */
    @Benchmark
    public final void benchmarkGenerateFiles() throws Exception {
        yosql.generateFiles();
    }

    protected static YoSql createYoSql(final ExecutionConfiguration configuration, final ExecutionErrors errors) {
        final SqlConfigurationFactory configurationFactory = new SqlConfigurationFactory(errors, configuration);
        final PrintStream output = null;
        // final Writer out = new BufferedWriter(new OutputStreamWriter(System.out));
        final SqlFileParser sqlFileParser = new Java8SqlFileParser(errors, configuration, configurationFactory, output);
        final ParserPreconditions preconditions = new ParserPreconditions(errors);
        // TODO: resolve files from classpath?
        final SqlFileResolver fileResolver = new PathBasedSqlFileResolver(preconditions, errors, configuration);
        final TypeWriter typeWriter = new TypeWriter(errors, output);
        final AnnotationGenerator annotations = new AnnotationGenerator(configuration);
        final TypicalFields fields = new TypicalFields(annotations);
        final LoggingGenerator logging = new JdkLoggingGenerator(fields);
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
        final Timer timer = new Timer(output);

        return new YoSql(fileResolver, sqlFileParser, repositoryGenerator, utilsGenerator, errors, timer);
    }

}
