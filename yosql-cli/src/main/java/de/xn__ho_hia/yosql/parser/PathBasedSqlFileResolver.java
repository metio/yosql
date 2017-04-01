package de.xn__ho_hia.yosql.parser;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;

import de.xn__ho_hia.yosql.generator.GeneratorPreconditions;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.ExecutionErrors;

/**
 * A SQL file resolver that starts at the given source, walks into every subdirectory and returns all .sql files.
 */
public final class PathBasedSqlFileResolver implements SqlFileResolver {

    private final GeneratorPreconditions preconditions;
    private final ExecutionErrors        errors;
    private final ExecutionConfiguration configuration;

    /**
     * @param preconditions
     *            The preconditions to use.
     * @param errors
     *            The error collector to use.
     * @param configuration
     *            The configuration to use.
     */
    @Inject
    public PathBasedSqlFileResolver(
            final GeneratorPreconditions preconditions,
            final ExecutionErrors errors,
            final ExecutionConfiguration configuration) {
        this.preconditions = preconditions;
        this.errors = errors;
        this.configuration = configuration;
    }

    @Override
    public List<Path> resolveFiles() {
        final Path source = configuration.inputBaseDirectory();
        preconditions.assertDirectoryIsReadable(source);

        final List<Path> result = new ArrayList<>();

        if (!errors.hasErrors()) {
            try (Stream<Path> paths = Files.walk(source, FileVisitOption.FOLLOW_LINKS)) {
                paths.filter(path -> Files.isRegularFile(path))
                        .filter(path -> path.toString().endsWith(".sql")) //$NON-NLS-1$
                        .forEach(result::add);
            } catch (final IOException | SecurityException exception) {
                errors.add(exception);
            }
        }

        return result;
    }

}
