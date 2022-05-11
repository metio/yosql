/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.tck;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.api.RxJavaMethodGenerator;
import wtf.metio.yosql.testing.configs.SqlConfigurations;

/**
 * Verifies that {@link RxJavaMethodGenerator}s work correctly.
 */
public interface RxJavaMethodGeneratorTCK {

    /**
     * @return A new {@link RxJavaMethodGenerator}.
     */
    RxJavaMethodGenerator generator();

    /**
     * @return The expected generated code for a RxJava read method.
     */
    String rxJavaReadMethodExpectation();

    /**
     * @return The expected generated code for a RxJava read method using a custom converter.
     */
    String rxJavaReadMethodUsingCustomConverterExpectation();

    @Test
    default void rxJavaReadMethod() {
        Assertions.assertEquals(
                rxJavaReadMethodExpectation(),
                generator().rxJavaReadMethod(
                                SqlConfigurations.sqlConfiguration(),
                                SqlConfigurations.sqlStatement())
                        .toString(),
                "The generated RxJava read method does not match expectation");
    }

    @Test
    default void rxJavaReadMethodUsingCustomConverter() {
        Assertions.assertEquals(
                rxJavaReadMethodUsingCustomConverterExpectation(),
                generator().rxJavaReadMethod(
                                SqlConfigurations.withCustomConverter(),
                                SqlConfigurations.sqlStatementWithCustomConverter())
                        .toString(),
                "The generated RxJava read method using a custom converter does not match expectation");
    }

}
