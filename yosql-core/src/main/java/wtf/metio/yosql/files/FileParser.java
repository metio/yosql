package wtf.metio.yosql.files;

import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;

public interface FileParser {

    List<SqlStatement> parseFiles();

}
