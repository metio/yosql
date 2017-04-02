package de.xn__ho_hia.yosql.benchmark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import de.xn__ho_hia.yosql.YoSql;
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
import de.xn__ho_hia.yosql.parser.ParserPreconditions;
import de.xn__ho_hia.yosql.parser.PathBasedSqlFileResolver;
import de.xn__ho_hia.yosql.parser.SqlConfigurationFactory;
import de.xn__ho_hia.yosql.parser.SqlFileParser;
import de.xn__ho_hia.yosql.parser.SqlFileResolver;
import de.xn__ho_hia.yosql.utils.Timer;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
abstract class AbstractGenerateFilesBenchmark {

    private Path tempDirectory;

    /**
     * @throws IOException
     *             In case anything goes wrong while creating a temporary directory.
     */
    @Setup
    @SuppressWarnings("nls")
    public void setup() throws IOException {
        tempDirectory = Files.createTempDirectory("yosql");
        Files.createDirectories(tempDirectory.resolve("input"));
        Files.createDirectories(tempDirectory.resolve("output"));
    }

    /**
     * Benchmarks code generation.
     *
     * @throws Exception
     *             In case anything goes wrong while generating files.
     */
    @Benchmark
    public final void benchmarkGenerateFiles() throws Exception {
        generateJavaFiles();
    }

    @SuppressWarnings({ "nls" })
    protected void generateSqlFiles(final int numberOfRepositories) throws IOException {
        final Path inputDirectory = tempDirectory.resolve("input");

        prepareRepository(numberOfRepositories, inputDirectory, "callFunction.sql");
        prepareRepository(numberOfRepositories, inputDirectory, "insertData.sql");
        prepareRepository(numberOfRepositories, inputDirectory, "readData.sql");
        prepareRepository(numberOfRepositories, inputDirectory, "updateData.sql");
    }

    @SuppressWarnings("nls")
    private void prepareRepository(
            final int numberOfRepositories,
            final Path inputDirectory,
            final String fileName) throws IOException {
        final InputStream updateData = getClass().getResourceAsStream("/sql-files/usecases/" + fileName);
        try (final BufferedReader insertDataReader = new BufferedReader(
                new InputStreamReader(updateData, StandardCharsets.UTF_8))) {
            final String insertDataRaw = insertDataReader.lines().collect(Collectors.joining("\n"));
            final Path insertDataSqlFile = tempDirectory.resolve(fileName);
            Files.write(insertDataSqlFile, insertDataRaw.getBytes(StandardCharsets.UTF_8));
            for (int index = 0; index < numberOfRepositories; index++) {
                final Path repositoryDirectory = inputDirectory.resolve("repository" + index);
                Files.createDirectories(repositoryDirectory);
                Files.copy(insertDataSqlFile, repositoryDirectory.resolve(fileName));
            }
        }
    }

    @SuppressWarnings("nls")
    protected void generateJavaFiles() throws InterruptedException, ExecutionException {
        final ExecutionConfiguration configuration = YoSql.prepareDefaultConfiguration()
                .setInputBaseDirectory(tempDirectory.resolve("input"))
                .setOutputBaseDirectory(tempDirectory.resolve("output"))
                .build();
        final ExecutionErrors errors = new ExecutionErrors();
        final YoSql yoSql = createYoSql(configuration, errors);
        yoSql.generateFiles();
    }

    protected static YoSql createYoSql(final ExecutionConfiguration configuration, final ExecutionErrors errors) {
        final SqlConfigurationFactory configurationFactory = new SqlConfigurationFactory(errors, configuration);
        final PrintStream output = null;
        // final Writer out = new BufferedWriter(new OutputStreamWriter(System.out));
        final SqlFileParser sqlFileParser = new SqlFileParser(errors, configuration, configurationFactory, output);
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
