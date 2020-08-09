package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.jdbcNamesConfiguration;

@DisplayName("DefaultJdbcStatementMethods")
class DefaultJdbcStatementMethodsTest extends ValidationFileTest {

    private DefaultJdbcStatementMethods generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcStatementMethods(jdbcNamesConfiguration());
    }

    @Test
    void executeQuery(final ValidationFile validationFile) {
        validate(generator.executeQuery(), validationFile);
    }

    @Test
    void addBatch(final ValidationFile validationFile) {
        validate(generator.addBatch(), validationFile);
    }

    @Test
    void executeBatch(final ValidationFile validationFile) {
        validate(generator.executeBatch(), validationFile);
    }

    @Test
    void executeUpdate(final ValidationFile validationFile) {
        validate(generator.executeUpdate(), validationFile);
    }

}