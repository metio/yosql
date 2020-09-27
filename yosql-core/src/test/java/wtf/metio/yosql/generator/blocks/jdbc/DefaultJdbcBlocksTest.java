package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.*;
import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.*;
import static wtf.metio.yosql.generator.logging.LoggingObjectMother.loggingGenerator;
import static wtf.metio.yosql.model.configuration.ModelConfigurationObjectMother.runtimeConfiguration;

@DisplayName("DefaultJdbcBlocks")
class DefaultJdbcBlocksTest {

    private DefaultJdbcBlocks generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcBlocks(
                runtimeConfiguration(),
                genericBlocks(),
                controlFlows(),
                names(),
                variables(),
                jdbcNames(),
                jdbcFields(),
                jdbcMethods(),
                loggingGenerator());
    }

    @Test
    void connectionVariable() {
        Assertions.assertEquals("""
                final java.sql.Connection connection = dataSource.getConnection()""", generator.connectionVariable().toString());
    }

}