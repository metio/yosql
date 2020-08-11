package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.model.sql.SqlConfiguration;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DefaultJdbcTransformer")
class DefaultJdbcTransformerTest {

    @Test
    void sqlException() {
        // given
        final var generator = new DefaultJdbcTransformer();

        // when
        final var exception = generator.sqlException(new SqlConfiguration());

        // then
        assertFalse(((Collection<?>) exception).isEmpty());
    }

    @Test
    void noSqlException() {
        // given
        final var generator = new DefaultJdbcTransformer();
        final var configuration = new SqlConfiguration();
        configuration.setMethodCatchAndRethrow(true);

        // when
        final var exception = generator.sqlException(configuration);

        // then
        assertTrue(((Collection<?>) exception).isEmpty());
    }

}