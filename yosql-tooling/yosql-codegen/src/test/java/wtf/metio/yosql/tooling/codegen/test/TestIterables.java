/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.test;

import org.junit.jupiter.api.Assertions;

public final class TestIterables {

    public static void assertIterable(Iterable<?> values, String... expectedValues) {
        int index = 0;
        for (final var value : values) {
            Assertions.assertEquals(value.toString(), expectedValues[index++]);
        }
    }

}
