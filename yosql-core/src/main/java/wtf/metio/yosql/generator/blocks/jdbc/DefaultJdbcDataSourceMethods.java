package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

final class DefaultJdbcDataSourceMethods implements JdbcMethods.JdbcDataSourceMethods {

    private final JdbcNamesConfiguration jdbcNames;

    DefaultJdbcDataSourceMethods(final JdbcNamesConfiguration jdbcNames) {
        this.jdbcNames = jdbcNames;
    }

    @Override
    public CodeBlock getConnection() {
        return CodeBlock.builder()
                .add("$N.getConnection()", jdbcNames.dataSource())
                .build();
    }

}
