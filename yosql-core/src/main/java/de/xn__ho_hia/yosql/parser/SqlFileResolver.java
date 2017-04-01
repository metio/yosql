package de.xn__ho_hia.yosql.parser;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 */
public interface SqlFileResolver {

    /**
     * @return A stream of SQL files found in source.
     */
    Stream<Path> resolveFiles();

}
