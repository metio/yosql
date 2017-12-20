/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.xn__ho_hia.yosql.generator.api.AnnotationGenerator;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;
import de.xn__ho_hia.yosql.model.PackageTypeSpec;
import de.xn__ho_hia.yosql.testutils.TestExecutionConfigurations;

@SuppressWarnings({ "nls", "static-method" })
class ToResultRowConverterGeneratorTest {

    @Test
    void shouldGenerateToResultRowConverterClass() {
        // given
        final ExecutionConfiguration config = TestExecutionConfigurations.testExecutionConfiguration();
        final AnnotationGenerator annotations = new AnnotationGenerator(config);
        final ToResultRowConverterGenerator generator = new ToResultRowConverterGenerator(annotations, config);

        // when
        final PackageTypeSpec typeSpec = generator.generateToResultRowConverterClass();

        // then
        Assertions.assertEquals("com.example.test.converter", typeSpec.getPackageName());
        Assertions.assertEquals("ToResultRowConverter", typeSpec.getType().name);
    }

}
