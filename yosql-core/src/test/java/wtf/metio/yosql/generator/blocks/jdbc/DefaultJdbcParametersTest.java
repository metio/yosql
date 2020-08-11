package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.parameters;
import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.jdbcNamesConfiguration;

@DisplayName("DefaultJdbcParameters")
class DefaultJdbcParametersTest extends ValidationFileTest {

    private DefaultJdbcParameters generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcParameters(parameters(), jdbcNamesConfiguration());
    }

    @Test
    void dataSource(final ValidationFile validationFile) {
        validate(generator.dataSource(), validationFile);
    }

    @Test
    void connection(final ValidationFile validationFile) {
        validate(generator.connection(), validationFile);
    }

    @Test
    void preparedStatement(final ValidationFile validationFile) {
        validate(generator.preparedStatement(), validationFile);
    }

    @Test
    void resultSet(final ValidationFile validationFile) {
        validate(generator.resultSet(), validationFile);
    }

    @Test
    void metaData(final ValidationFile validationFile) {
        validate(generator.metaData(), validationFile);
    }

    @Test
    void columnCount(final ValidationFile validationFile) {
        validate(generator.columnCount(), validationFile);
    }

    @Test
    void index(final ValidationFile validationFile) {
        validate(generator.index(), validationFile);
    }

    @Test
    void columnLabel(final ValidationFile validationFile) {
        validate(generator.columnLabel(), validationFile);
    }

}