package de.xn__ho_hia.yosql.parser;

import java.nio.file.Path;
import java.util.List;

/**
 */
public interface SqlFileResolver {

    /**
     * @return A stream of SQL files found in source.
     */
    List<Path> resolveFiles();

}
