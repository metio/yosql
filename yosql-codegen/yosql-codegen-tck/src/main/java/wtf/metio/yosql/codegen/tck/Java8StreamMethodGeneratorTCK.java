/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.tck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.api.Java8StreamMethodGenerator;
import wtf.metio.yosql.testing.configs.SqlConfigurations;

/**
 * Verifies that {@link Java8StreamMethodGenerator}s work correctly.
 */
public interface Java8StreamMethodGeneratorTCK {

    /**
     * @return A new {@link Java8StreamMethodGenerator}.
     */
    Java8StreamMethodGenerator generator();

    /**
     * @return The expected generated code for a stream read method.
     */
    String streamReadMethodExpectation();

    /**
     * @return The expected generated code for a stream read method using a custom converter.
     */
    String streamReadMethodUsingCustomConverterExpectation();

    @Test
    default void streamReadMethod() {
        Assertions.assertEquals(
                streamReadMethodExpectation(),
                generator().streamReadMethod(
                                SqlConfigurations.sqlConfiguration(),
                                SqlConfigurations.sqlStatement())
                        .toString(),
                "The generated stream read method does not match expectation");
    }

    @Test
    default void streamReadMethodUsingCustomConverter() {
        Assertions.assertEquals(
                streamReadMethodUsingCustomConverterExpectation(),
                generator().streamReadMethod(
                                SqlConfigurations.withCustomConverter(),
                                SqlConfigurations.sqlStatementWithCustomConverter())
                        .toString(),
                "The generated stream read method using a custom converter does not match expectation");
    }

}
