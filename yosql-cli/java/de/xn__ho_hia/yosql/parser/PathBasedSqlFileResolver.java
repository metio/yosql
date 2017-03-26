package de.xn__ho_hia.yosql.parser;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.xn__ho_hia.yosql.model.SqlSourceFile;

/**
 * A SQL file resolver that starts at the given source, walks into every subdirectory and returns all .sql files.
 */
public final class PathBasedSqlFileResolver implements SqlFileResolver<Path> {

    @Override
    public Stream<SqlSourceFile> resolveFiles(final Path source) {
        final List<SqlSourceFile> result = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(source, FileVisitOption.FOLLOW_LINKS)) {
            paths.filter(path -> Files.isRegularFile(path))
                    .filter(path -> path.toString().endsWith(".sql")) //$NON-NLS-1$
                    .map(path -> new SqlSourceFile(path, source))
                    .forEach(result::add);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return result.stream();
    }

}
