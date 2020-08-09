package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.jdbcNamesConfiguration;

@DisplayName("DefaultJdbcDataSourceMethods")
class DefaultJdbcDataSourceMethodsTest extends ValidationFileTest {

    @Test
    void getConnection(final ValidationFile validationFile) {
        // given
        final var generator = new DefaultJdbcDataSourceMethods(jdbcNamesConfiguration());

        // when
        final var connection = generator.getConnection();

        // then
        validate(connection, validationFile);
    }

}