/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;

import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

class DefaultMethodParameterConfigurerTest {

    private MethodParameterConfigurer configurer;

    @BeforeEach
    void setUp() {
        configurer = FilesObjectMother.methodParameterConfigurer();
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
