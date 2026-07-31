/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.blocks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DefaultCodeBlocks")
class DefaultCodeBlocksTest {

    private CodeBlocks blocks;

    @BeforeEach
    void setUp() {
        blocks = new DefaultCodeBlocks();
    }

    @Test
    void returnTrue() {
        Assertions.assertEquals("""
                return true;
                """, blocks.returnTrue().toString());
    }

    @Test
    void returnFalse() {
        Assertions.assertEquals("""
                return false;
                """, blocks.returnFalse().toString());
    }

    @Test
    void initializeFieldToSelf() {
        Assertions.assertEquals("""
                this.name = name;
                """, blocks.initializeFieldToSelf("name").toString());
    }

    @Test
    void close() {
        Assertions.assertEquals("""
                resource.close();
                """, blocks.close("resource").toString());
    }

    @Test
    void returnValue() {
        Assertions.assertEquals("""
                return value;
                """, blocks.returnValue(CodeBlocks.code("value")).toString());
    }

}
