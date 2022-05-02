/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.tck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.api.FieldsGenerator;
import wtf.metio.yosql.internals.junit5.TestIterables;
import wtf.metio.yosql.testing.configs.SqlConfigurations;

/**
 * Verifies that {@link FieldsGenerator}s work correctly.
 */
public interface FieldsGeneratorTCK {

    FieldsGenerator generator();

    String staticInitializerExpectation();

    String[] asFieldsExpectations();

    @Test
    default void staticInitializer() {
        Assumptions.assumeTrue(generator().staticInitializer(SqlConfigurations.sqlStatements()).isPresent());
        Assertions.assertEquals(
                staticInitializerExpectation(),
                generator().staticInitializer(SqlConfigurations.sqlStatements()).get().toString(),
                "The generated static initializer did not match expectation");
    }

    @Test
    default void asFields() {
        TestIterables.assertIterable(generator().asFields(SqlConfigurations.sqlStatements()), asFieldsExpectations());
    }

}
