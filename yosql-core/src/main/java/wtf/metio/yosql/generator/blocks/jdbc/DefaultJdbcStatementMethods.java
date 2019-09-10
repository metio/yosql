package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

import static wtf.metio.yosql.generator.blocks.jdbc.JdbcMethodNames.*;

final class DefaultJdbcStatementMethods implements JdbcMethods.JdbcStatementMethods {

    private final JdbcNamesConfiguration jdbcNames;

    DefaultJdbcStatementMethods(final JdbcNamesConfiguration jdbcNames) {
        this.jdbcNames = jdbcNames;
    }

    @Override
    public CodeBlock executeQuery() {
        return CodeBlock.builder()
                .add("$N.$N()", jdbcNames.statement(), EXECUTE_QUERY)
                .build();
    }

    @Override
    public CodeBlock executeUpdate() {
        return CodeBlock.builder()
                .add("$N.$N()", jdbcNames.statement(), EXECUTE_UPDATE)
                .build();
    }

    @Override
    public CodeBlock executeBatch() {
        return CodeBlock.builder()
                .add("$N.$N()", jdbcNames.statement(), EXECUTE_BATCH)
                .build();
    }

    @Override
    public CodeBlock addBatch() {
        return CodeBlock.builder()
                .add("$N.$N()", jdbcNames.statement(), ADD_BATCH)
                .build();
    }

}
