package de.xn__ho_hia.yosql.parser;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.model.SqlSourceFile;

public interface SqlFileResolver<SOURCE> {

	Stream<SqlSourceFile> resolveFiles(SOURCE source);
	
}
