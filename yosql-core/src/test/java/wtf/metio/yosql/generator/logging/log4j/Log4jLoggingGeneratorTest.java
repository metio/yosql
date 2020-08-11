package wtf.metio.yosql.generator.logging.log4j;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.fields;
import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;

@DisplayName("Log4jLoggingGenerator")
class Log4jLoggingGeneratorTest extends ValidationFileTest {

    private Log4jLoggingGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new Log4jLoggingGenerator(names(), fields());
    }

    @Test
    void logger(final ValidationFile validationFile) {
        final var logger = generator.logger(TypeName.BOOLEAN);
        Assertions.assertTrue(logger.isPresent());
        validate(logger.get(), validationFile);
    }

    @Test
    void queryPicked(final ValidationFile validationFile) {
        validate(generator.queryPicked("test"), validationFile);
    }

    @Test
    void indexPicked(final ValidationFile validationFile) {
        validate(generator.indexPicked("test"), validationFile);
    }

    @Test
    void vendorQueryPicked(final ValidationFile validationFile) {
        validate(generator.vendorQueryPicked("test"), validationFile);
    }

    @Test
    void vendorIndexPicked(final ValidationFile validationFile) {
        validate(generator.vendorIndexPicked("test"), validationFile);
    }

    @Test
    void vendorDetected(final ValidationFile validationFile) {
        validate(generator.vendorDetected(), validationFile);
    }

    @Test
    void executingQuery(final ValidationFile validationFile) {
        validate(generator.executingQuery(), validationFile);
    }

    @Test
    void shouldLog(final ValidationFile validationFile) {
        validate(generator.shouldLog(), validationFile);
    }

    @Test
    void isEnabled() {
        Assertions.assertTrue(generator.isEnabled());
    }

    @Test
    void entering(final ValidationFile validationFile) {
        validate(generator.entering("repo", "method"), validationFile);
    }

}