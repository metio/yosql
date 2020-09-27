package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.parameters;
import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.jdbcNamesConfiguration;

@DisplayName("DefaultJdbcParameters")
class DefaultJdbcParametersTest {

    private DefaultJdbcParameters generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcParameters(parameters(), jdbcNamesConfiguration());
    }

    @Test
    void dataSource() {
        Assertions.assertEquals("""
                final javax.sql.DataSource dataSource""", generator.dataSource().toString());
    }

    @Test
    void connection() {
        Assertions.assertEquals("""
                final java.sql.Connection connection""", generator.connection().toString());
    }

    @Test
    void preparedStatement() {
        Assertions.assertEquals("""
                final java.sql.PreparedStatement statement""", generator.preparedStatement().toString());
    }

    @Test
    void resultSet() {
        Assertions.assertEquals("""
                final java.sql.ResultSet resultSet""", generator.resultSet().toString());
    }

    @Test
    void metaData() {
        Assertions.assertEquals("""
                final java.sql.ResultSetMetaData metaData""", generator.metaData().toString());
    }

    @Test
    void columnCount() {
        Assertions.assertEquals("""
                final int columnCount""", generator.columnCount().toString());
    }

    @Test
    void index() {
        Assertions.assertEquals("""
                final int index""", generator.index().toString());
    }

    @Test
    void columnLabel() {
        Assertions.assertEquals("""
                final java.lang.String columnLabel""", generator.columnLabel().toString());
    }

}