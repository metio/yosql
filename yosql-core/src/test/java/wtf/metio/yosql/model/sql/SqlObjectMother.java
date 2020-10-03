package wtf.metio.yosql.model.sql;

import java.nio.file.Paths;
import java.util.List;

public final class SqlObjectMother {
    
    public static SqlConfiguration sqlConfiguration() {
        final var config = new SqlConfiguration();
        config.setName("queryTest");
        config.getParameters().add(SqlParameter.builder()
                .setName("test")
                .setType(Object.class.getName())
                .setIndices(0)
                .setConverter("")
                .build());
        return config;
    }
    
    public static List<SqlStatement> sqlStatements() {
        return List.of(new SqlStatement(Paths.get("/some/path/query.sql"), sqlConfiguration(), "SELECT raw FROM table;"));
    }

    private SqlObjectMother() {
        // factory class
    }
    
}
