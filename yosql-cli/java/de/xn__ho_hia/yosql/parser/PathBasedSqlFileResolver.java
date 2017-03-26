package de.xn__ho_hia.yosql.parser;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import de.xn__ho_hia.yosql.model.SqlSourceFile;

public class PathBasedSqlFileResolver implements SqlFileResolver<Path> {

	@Override
	public Stream<SqlSourceFile> resolveFiles(Path source) {
		List<SqlSourceFile> result = new ArrayList<>();

		try (Stream<Path> paths = Files.walk(source, FileVisitOption.FOLLOW_LINKS)) {
			paths.filter(path -> Files.isRegularFile(path))
				.filter(path -> path.toString().endsWith(".sql"))
				.map(path -> new SqlSourceFile(path, source))
				.forEach(result::add);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.stream();
	}

}
