package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;
import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.jdbcNamesConfiguration;

@DisplayName("DefaultJdbcConnectionMethods")
class DefaultJdbcConnectionMethodsTest extends ValidationFileTest {

    private DefaultJdbcConnectionMethods generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcConnectionMethods(names(), jdbcNamesConfiguration());
    }

    @Test
    void prepareStatement(final ValidationFile validationFile) {
        validate(generator.prepareStatement(), validationFile);
    }

    @Test
    void prepareCallable(final ValidationFile validationFile) {
        validate(generator.prepareCallable(), validationFile);
    }

}