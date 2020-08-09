package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.jdbcNamesConfiguration;

@DisplayName("DefaultJdbcResultSetMethods")
class DefaultJdbcResultSetMethodsTest extends ValidationFileTest {

    @Test
    void getMetaData(final ValidationFile validationFile) {
        // given
        final var generator = new DefaultJdbcResultSetMethods(jdbcNamesConfiguration());

        // when
        final var metaData = generator.getMetaData();

        // then
        validate(metaData, validationFile);
    }

}