package de.xn__ho_hia.yosql.parser;

import java.util.stream.Stream;

import de.xn__ho_hia.yosql.model.SqlSourceFile;

/**
 * @param <SOURCE>
 *            The type of the input source (e.g. {@link java.nio.file.Path})
 */
public interface SqlFileResolver<SOURCE> {

    /**
     * @param source
     *            The source to read.
     * @return A stream of SQL files found in source.
     */
    Stream<SqlSourceFile> resolveFiles(SOURCE source);

}
