package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

import static wtf.metio.yosql.generator.blocks.jdbc.JdbcMethodNames.GET_CONNECTION;

final class DefaultJdbcDataSourceMethods implements JdbcMethods.JdbcDataSourceMethods {

    private final JdbcNamesConfiguration jdbcNames;

    DefaultJdbcDataSourceMethods(final JdbcNamesConfiguration jdbcNames) {
        this.jdbcNames = jdbcNames;
    }

    @Override
    public CodeBlock getConnection() {
        return CodeBlock.builder()
                .add("$N.$N()", jdbcNames.dataSource(), GET_CONNECTION)
                .build();
    }

}
