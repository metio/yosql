package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.*;

@DisplayName("DefaultJdbcMethods")
class DefaultJdbcMethodsTest {

    private DefaultJdbcMethods generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcMethods(
                jdbcDataSourceMethods(),
                jdbcConnectionMethods(),
                jdbcResultSetMethods(),
                jdbcMetaDataMethods(),
                jdbcStatementMethods());
    }

    @Test
    void dataSource() {
        assertNotNull(generator.dataSource());
    }

    @Test
    void connection() {
        assertNotNull(generator.connection());
    }

    @Test
    void metaData() {
        assertNotNull(generator.metaData());
    }

    @Test
    void resultSet() {
        assertNotNull(generator.resultSet());
    }

    @Test
    void statement() {
        assertNotNull(generator.statement());
    }

}