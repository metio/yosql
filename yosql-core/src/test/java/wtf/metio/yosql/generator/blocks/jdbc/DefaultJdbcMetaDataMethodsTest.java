package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.jdbcNamesConfiguration;

@DisplayName("DefaultJdbcMetaDataMethods")
class DefaultJdbcMetaDataMethodsTest {

    @Test
    void getColumnCount() {
        // given
        final var generator = new DefaultJdbcMetaDataMethods(jdbcNamesConfiguration());

        // when
        final var columnCount = generator.getColumnCount();

        // then
        Assertions.assertEquals("""
                metaData.getColumnCount()""", columnCount.toString());
    }

}