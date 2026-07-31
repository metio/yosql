/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.codegen.orchestration.OrchestrationObjectMother;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;

import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

@DisplayName("DefaultMethodParameterConfigurer")
class DefaultMethodParameterConfigurerTest {

    private DefaultMethodParameterConfigurer configurer;

    @BeforeEach
    void setUp() {
        configurer = new DefaultMethodParameterConfigurer(
                LoggingObjectMother.logger(),
                OrchestrationObjectMother.executionErrors(),
                LoggingObjectMother.messages());
    }

    @Test
    void configureParameters() {
        final var configuration = SqlConfigurations.simpleSqlConfiguration();
        final var source = Paths.get("test.sql");
        final var indices = new LinkedHashMap<String, List<Integer>>();
        indices.put("first", List.of(1, 3));
        indices.put("second", List.of(2, 4));
        final var adapted = configurer.configureParameters(configuration, source, indices);

        Assertions.assertEquals(2, adapted.parameters().size());
        Assertions.assertAll(
                () -> Assertions.assertEquals("first", adapted.parameters().get(0).name().get()),
                () -> Assertions.assertEquals("second", adapted.parameters().get(1).name().get()));
    }

}
