package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

final class DefaultJdbcResultSetMethods implements JdbcMethods.JdbcResultSetMethods {

    private final JdbcNamesConfiguration jdbcNames;

    DefaultJdbcResultSetMethods(final JdbcNamesConfiguration jdbcNames) {
        this.jdbcNames = jdbcNames;
    }

    @Override
    public CodeBlock getMetaData() {
        return CodeBlock.builder()
                .add("$N.getMetaData()", jdbcNames.resultSet())
                .build();
    }

}
