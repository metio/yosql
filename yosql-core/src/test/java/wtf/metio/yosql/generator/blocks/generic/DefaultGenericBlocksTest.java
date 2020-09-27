package wtf.metio.yosql.generator.blocks.generic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.generator.blocks.api.GenericBlocks;

@DisplayName("DefaultGenericBlocks")
class DefaultGenericBlocksTest {

    private GenericBlocks blocks;

    @BeforeEach
    void setUp() {
        blocks = new DefaultGenericBlocks();
    }

    @Test
    @DisplayName("returns true")
    void shouldReturnTrue() {
        Assertions.assertEquals("""
                return true;
                """, blocks.returnTrue().toString());
    }

    @Test
    @DisplayName("returns false")
    void shouldReturnFalse() {
        Assertions.assertEquals("""
                return false;
                """, blocks.returnFalse().toString());
    }

    @Test
    @DisplayName("initialize fields")
    void shouldInitializeField() {
        Assertions.assertEquals("""
                this.name = name;
                """, blocks.initializeFieldToSelf("name").toString());
    }

    @Test
    @DisplayName("close resources")
    void shouldCloseResource() {
        Assertions.assertEquals("""
                resource.close();
                """, blocks.close("resource").toString());
    }

    @Test
    @DisplayName("returns value")
    void shouldReturnValue() {
        Assertions.assertEquals("""
                return value;
                """, blocks.returnValue(CodeBlocks.code("value")).toString());
    }

}