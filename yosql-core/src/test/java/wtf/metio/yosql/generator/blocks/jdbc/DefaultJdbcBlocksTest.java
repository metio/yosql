package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.*;
import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.*;
import static wtf.metio.yosql.generator.logging.LoggingObjectMother.loggingGenerator;

@DisplayName("DefaultJdbcBlocks")
class DefaultJdbcBlocksTest {

    private DefaultJdbcBlocks generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcBlocks(
                null,
                genericBlocks(),
                controlFlows(),
                names(),
                variables(),
                jdbcNames(),
                jdbcFields(),
                jdbcMethods(),
                loggingGenerator());
    }

}