package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

import static wtf.metio.yosql.generator.blocks.jdbc.JdbcMethodNames.GET_COLUMN_COUNT;

final class DefaultJdbcMetaDataMethods implements JdbcMethods.JdbcMetaDataMethods {

    private final JdbcNamesConfiguration jdbcNames;

    DefaultJdbcMetaDataMethods(final JdbcNamesConfiguration jdbcNames) {
        this.jdbcNames = jdbcNames;
    }

    @Override
    public CodeBlock getColumnCount() {
        return CodeBlock.builder()
                .add("$N.$N()", jdbcNames.metaData(), GET_COLUMN_COUNT)
                .build();
    }

}
