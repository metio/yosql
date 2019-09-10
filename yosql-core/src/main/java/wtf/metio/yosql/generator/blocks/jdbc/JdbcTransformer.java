package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.model.sql.SqlConfiguration;

public interface JdbcTransformer {

    Iterable<TypeName> sqlException(SqlConfiguration configuration);

}
