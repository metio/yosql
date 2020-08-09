package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.ClassName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

@DisplayName("DefaultClasses")
class DefaultClassesTest extends ValidationFileTest {
    
    private DefaultClasses generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultClasses();
    }

    @Test
    @DisplayName("generates open classes")
    void shouldGenerateOpenClass(final ValidationFile validationFile) {
        validate(generator.openClass(ClassName.bestGuess("TestClass")).build(), validationFile);
    }

    @Test
    @DisplayName("generates public classes")
    void shouldGeneratePublicClass(final ValidationFile validationFile) {
        validate(generator.publicClass(ClassName.bestGuess("TestClass")).build(), validationFile);
    }

}