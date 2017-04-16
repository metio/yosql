package de.xn__ho_hia.yosql.benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

/**
 * Encapsulates common benchmark functionality.
 */
@State(Scope.Benchmark)
public abstract class AbstractBenchmark {

    private static final String         BENCHMARK_ROOT_DIRECTORY = "yosql-benchmark"; //$NON-NLS-1$
    private static final String         INPUT_DIRECTORY          = "sql-files";       //$NON-NLS-1$
    private static final String         OUTPUT_DIRECTORY         = "output";          //$NON-NLS-1$

    @SuppressWarnings("nls")
    protected static final List<String> SUPPORTED_USE_CASES      = Arrays.asList(
            "callFunction.sql",
            "callFunctionMultiple.sql",
            "insertData.sql",
            "insertDataMultiple.sql",
            "readData.sql",
            "readDataMultiple.sql",
            "updateData.sql",
            "updateDataMultiple.sql");

    private Path                        tempDirectory;
    protected Path                      inputDirectory;
    protected Path                      outputDirectory;

    /**
     * @throws IOException
     *             In case anything goes wrong while creating a temporary directory.
     */
    @Setup
    public void setup() throws IOException {
        tempDirectory = Files.createTempDirectory(BENCHMARK_ROOT_DIRECTORY);
        inputDirectory = tempDirectory.resolve(INPUT_DIRECTORY);
        outputDirectory = tempDirectory.resolve(OUTPUT_DIRECTORY);
        Files.createDirectories(inputDirectory);
        Files.createDirectories(outputDirectory);
    }

    /**
     * @throws IOException
     *             In case anything goes wrong while cleaning up the temporary directory.
     */
    @TearDown
    public void tearDown() throws IOException {
        try (Stream<Path> files = Files.walk(tempDirectory)) {
            files
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    protected void prepareRepositoriesForAllUseCases(final int numberOfRepositories) {
        SUPPORTED_USE_CASES.forEach(usecase -> prepareRepositories(numberOfRepositories, usecase));
    }

    @SuppressWarnings("nls")
    protected void prepareRepositories(
            final int numberOfRepositories,
            final String fileName) {
        final InputStream updateData = getClass().getResourceAsStream("/sql-files/usecases/" + fileName);
        try (final BufferedReader insertDataReader = new BufferedReader(
                new InputStreamReader(updateData, StandardCharsets.UTF_8))) {
            final String insertDataRaw = insertDataReader.lines().collect(Collectors.joining("\n"));
            final Path insertDataSqlFile = tempDirectory.resolve(fileName);
            Files.write(insertDataSqlFile, insertDataRaw.getBytes(StandardCharsets.UTF_8));
            for (int index = 1; index <= numberOfRepositories; index++) {
                final Path repositoryDirectory = inputDirectory.resolve(repositoryName(index));
                Files.createDirectories(repositoryDirectory);
                Files.copy(insertDataSqlFile, repositoryDirectory.resolve(fileName));
            }
        } catch (final IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    protected final static String repositoryName(final int index) {
        return "repository" + index; //$NON-NLS-1$
    }

}