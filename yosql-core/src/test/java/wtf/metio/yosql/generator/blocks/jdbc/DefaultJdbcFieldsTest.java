package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.jdbcFieldsConfiguration;

@DisplayName("DefaultJdbcFields")
class DefaultJdbcFieldsTest extends ValidationFileTest {

    private DefaultJdbcFields generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcFields(jdbcFieldsConfiguration());
    }

    @Test
    void constantSqlStatementFieldName(final ValidationFile validationFile) {
        // given
        final var config = new SqlConfiguration();
        config.setName("test");

        // when
        final var constant = generator.constantSqlStatementFieldName(config);

        // then
        assertEquals(validationFile.read(), constant);
    }

    @Test
    void constantSqlStatementFieldNameWithVendor(final ValidationFile validationFile) {
        // given
        final var config = new SqlConfiguration();
        config.setName("test");
        config.setVendor("MyDB");

        // when
        final var constant = generator.constantSqlStatementFieldName(config);

        // then
        assertEquals(validationFile.read(), constant);
    }

    @Test
    void constantRawSqlStatementFieldName(final ValidationFile validationFile) {
        // given
        final var config = new SqlConfiguration();
        config.setName("test");

        // when
        final var constant = generator.constantRawSqlStatementFieldName(config);

        // then
        assertEquals(validationFile.read(), constant);
    }

    @Test
    void constantRawSqlStatementFieldNameWithVendor(final ValidationFile validationFile) {
        // given
        final var config = new SqlConfiguration();
        config.setName("test");
        config.setVendor("Delphi DB");

        // when
        final var constant = generator.constantRawSqlStatementFieldName(config);

        // then
        assertEquals(validationFile.read(), constant);
    }

    @Test
    void constantSqlStatementParameterIndexFieldName(final ValidationFile validationFile) {
        // given
        final var config = new SqlConfiguration();
        config.setName("test");

        // when
        final var constant = generator.constantSqlStatementParameterIndexFieldName(config);

        // then
        assertEquals(validationFile.read(), constant);
    }

    @Test
    void constantSqlStatementParameterIndexFieldNameWithVendor(final ValidationFile validationFile) {
        // given
        final var config = new SqlConfiguration();
        config.setName("testQueryStatement");
        config.setVendor("PregreSQL");

        // when
        final var constant = generator.constantSqlStatementParameterIndexFieldName(config);

        // then
        assertEquals(validationFile.read(), constant);
    }

}