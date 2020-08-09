package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

final class DefaultJdbcMetaDataMethods implements JdbcMethods.JdbcMetaDataMethods {

    private final JdbcNamesConfiguration jdbcNames;

    DefaultJdbcMetaDataMethods(final JdbcNamesConfiguration jdbcNames) {
        this.jdbcNames = jdbcNames;
    }

    @Override
    public CodeBlock getColumnCount() {
        return CodeBlock.builder()
                .add("$N.getColumnCount()", jdbcNames.metaData())
                .build();
    }

}
