package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.ClassName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DefaultClasses")
class DefaultClassesTest {

    private DefaultClasses generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultClasses();
    }

    @Test
    @DisplayName("generates open classes")
    void shouldGenerateOpenClass() {
        Assertions.assertEquals("""
                public class TestClass {
                }
                """, generator.openClass(ClassName.bestGuess("TestClass")).build().toString());
    }

    @Test
    @DisplayName("generates public classes")
    void shouldGeneratePublicClass() {
        Assertions.assertEquals("""
                public final class TestClass {
                }
                """, generator.publicClass(ClassName.bestGuess("TestClass")).build().toString());
    }

}