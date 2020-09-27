package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.CodeBlock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;
import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.variables;

@DisplayName("DefaultControlFlows")
class DefaultControlFlowsTest {

    private DefaultControlFlows generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultControlFlows(variables(), names());
    }

    @Test
    @DisplayName("try with resource")
    void shouldTryResource() {
        Assertions.assertEquals("""
                try (resource) {
                """, generator.tryWithResource(CodeBlocks.code("resource")).toString());
    }

    @Test
    @DisplayName("end try block")
    void shouldEndTry() {
        Assertions.assertEquals("""
                try (resource) {
                }
                """, CodeBlock.builder()
                .add(generator.tryWithResource(CodeBlocks.code("resource")))
                .add(generator.endTryBlock())
                .build().toString());
    }

    @Test
    @DisplayName("if has next")
    void shouldProceedForNext() {
        Assertions.assertEquals("""
                if (state.next()) {
                """, generator.ifHasNext().toString());
    }

}