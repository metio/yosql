package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.jdbcNamesConfiguration;

@DisplayName("DefaultJdbcMetaDataMethods")
class DefaultJdbcMetaDataMethodsTest extends ValidationFileTest {

    @Test
    void getColumnCount(final ValidationFile validationFile) {
        // given
        final var generator = new DefaultJdbcMetaDataMethods(jdbcNamesConfiguration());

        // when
        final var columnCount = generator.getColumnCount();

        // then
        validate(columnCount, validationFile);
    }

}