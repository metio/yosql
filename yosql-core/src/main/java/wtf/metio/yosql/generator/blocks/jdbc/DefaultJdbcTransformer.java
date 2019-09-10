package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.model.sql.SqlConfiguration;

import java.sql.SQLException;
import java.util.Collections;

final class DefaultJdbcTransformer implements JdbcTransformer {

    @Override
    public Iterable<TypeName> sqlException(final SqlConfiguration configuration) {
        if (!configuration.isMethodCatchAndRethrow()) {
            return Collections.singletonList(ClassName.get(SQLException.class));
        }
        return Collections.emptyList();
    }

}
