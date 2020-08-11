package wtf.metio.yosql.generator.logging.slf4j;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.fields;
import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;

@DisplayName("Slf4jLoggingGenerator")
class Slf4jLoggingGeneratorTest extends ValidationFileTest {

    private Slf4jLoggingGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new Slf4jLoggingGenerator(names(), fields());
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