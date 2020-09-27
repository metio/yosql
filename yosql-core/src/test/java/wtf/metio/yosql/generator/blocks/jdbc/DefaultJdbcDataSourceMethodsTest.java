package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.jdbcNamesConfiguration;

@DisplayName("DefaultJdbcDataSourceMethods")
class DefaultJdbcDataSourceMethodsTest {

    @Test
    void getConnection() {
        // given
        final var generator = new DefaultJdbcDataSourceMethods(jdbcNamesConfiguration());

        // when
        final var connection = generator.getConnection();

        // then
        Assertions.assertEquals("""
                dataSource.getConnection()""", connection.toString());
    }

}