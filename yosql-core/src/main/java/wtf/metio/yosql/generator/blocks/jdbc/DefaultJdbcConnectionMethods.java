package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.generator.blocks.api.Names;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

import static wtf.metio.yosql.generator.blocks.jdbc.JdbcMethodNames.PREPARE_CALL;
import static wtf.metio.yosql.generator.blocks.jdbc.JdbcMethodNames.PREPARE_STATEMENT;

final class DefaultJdbcConnectionMethods implements JdbcMethods.JdbcConnectionMethods {

    private final Names names;
    private final JdbcNamesConfiguration jdbcNames;

    DefaultJdbcConnectionMethods(final Names names, final JdbcNamesConfiguration jdbcNames) {
        this.names = names;
        this.jdbcNames = jdbcNames;
    }

    @Override
    public CodeBlock prepareStatement() {
        return CodeBlock.builder()
                .add("$N.$N($N)", jdbcNames.connection(), PREPARE_STATEMENT, names.query())
                .build();
    }

    @Override
    public CodeBlock prepareCallable() {
        return CodeBlock.builder()
                .add("$N.$N($N)", jdbcNames.connection(), PREPARE_CALL, names.query())
                .build();
    }

}
