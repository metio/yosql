package wtf.metio.yosql.generator.logging.noop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NoOpLoggingGenerator")
class NoOpLoggingGeneratorTest {

    private NoOpLoggingGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new NoOpLoggingGenerator();
    }

    @Test
    void isEnabled() {
        Assertions.assertFalse(generator.isEnabled());
    }

}