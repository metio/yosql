/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.utilities;

import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

import ch.qos.cal10n.MessageConveyor;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.model.ExecutionConfiguration;
import wtf.metio.yosql.model.PackageTypeSpec;
import wtf.metio.yosql.testutils.TestExecutionConfigurations;

class ToResultRowConverterGeneratorTest {

    @Test
    void shouldGenerateToResultRowConverterClass() {
        // given
        final ExecutionConfiguration config = TestExecutionConfigurations.testExecutionConfiguration();
        final AnnotationGenerator annotations = new AnnotationGenerator(config);
        final LocLogger logger = new LocLoggerFactory(new MessageConveyor(Locale.ENGLISH))
                .getLocLogger(ToResultRowConverterGeneratorTest.class);
        final ToResultRowConverterGenerator generator = new ToResultRowConverterGenerator(annotations, config, logger);

        // when
        final PackageTypeSpec typeSpec = generator.generateToResultRowConverterClass();

        // then
        Assertions.assertEquals("com.example.test.converter", typeSpec.getPackageName());
        Assertions.assertEquals("ToResultRowConverter", typeSpec.getType().name);
    }

}
