/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultRowConverterTest {

    @Test
    void fromString() {
        final var alias = "test";
        final var converterType = "com.example.MyConverter";
        final var methodName = "asUserType";
        final var resultType = "com.example.MyResult";
        final var input = String.format("%s:%s:%s:%s", alias, converterType, methodName, resultType);
        final var converter = ResultRowConverter.fromString(input);

        assertNotNull(converter);
        assertAll(
                () -> assertEquals(alias, converter.alias().orElseThrow()),
                () -> assertEquals(converterType, converter.converterType().orElseThrow()),
                () -> assertEquals(methodName, converter.methodName().orElseThrow()),
                () -> assertEquals(resultType, converter.resultType().orElseThrow()));
    }

}
