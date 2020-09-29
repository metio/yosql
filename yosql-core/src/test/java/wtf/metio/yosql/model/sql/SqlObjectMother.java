package wtf.metio.yosql.model.sql;

import java.nio.file.Paths;
import java.util.List;

public final class SqlObjectMother {
    
    public static SqlConfiguration sqlConfiguration() {
        final var config = new SqlConfiguration();
        config.setName("queryTest");
        final var parameter = new SqlParameter();
        parameter.setName("test");
        config.getParameters().add(parameter);
        return config;
    }
    
    public static List<SqlStatement> sqlStatements() {
        return List.of(new SqlStatement(Paths.get("/some/path/query.sql"), sqlConfiguration(), "SELECT raw FROM table;"));
    }

    private SqlObjectMother() {
        // factory class
    }
    
}
