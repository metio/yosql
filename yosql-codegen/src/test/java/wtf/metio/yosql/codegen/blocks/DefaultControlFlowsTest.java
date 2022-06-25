/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.CodeBlock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.JavaConfigurations;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;

@DisplayName("DefaultControlFlows")
class DefaultControlFlowsTest {

    private DefaultControlFlows generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultControlFlows(new DefaultVariables(JavaConfigurations.defaults()), NamesConfigurations.defaults());
    }

    @Test
    void shouldTryResource() {
        Assertions.assertEquals("""
                try (resource) {
                """, generator.tryWithResource(CodeBlocks.code("resource")).toString());
    }

    @Test
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
    void ifHasNext() {
        Assertions.assertEquals("""
                if (resultSet.next()) {
                """, generator.ifHasNext().toString());
    }

}
