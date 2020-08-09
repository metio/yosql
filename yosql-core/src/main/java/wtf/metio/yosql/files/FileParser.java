package wtf.metio.yosql.files;

import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;

/**
 * High-level interface that handles parsing of SQL files.
 * 
 * @see SqlFileResolver
 * @see SqlFileParser
 */
public interface FileParser {

    /**
     * @return All files found in the configured input directory.
     */
    List<SqlStatement> parseFiles();

}
