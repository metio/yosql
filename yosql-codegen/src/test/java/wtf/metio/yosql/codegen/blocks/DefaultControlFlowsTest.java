/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
