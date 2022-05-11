/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.junit5;

import org.junit.jupiter.api.Assertions;

import java.util.stream.StreamSupport;

/**
 * Additional assertions for {@link Iterable}s.
 */
public final class TestIterables {

    /**
     * Asserts that the expected values are equal to the actual values. The actual values are converted to
     * {@link String}s first.
     *
     * @param expectedValues The expected values.
     * @param actualValues   The actual values.
     */
    public static void assertIterables(final Iterable<String> expectedValues, final Iterable<?> actualValues) {
        final var strings = StreamSupport.stream(actualValues.spliterator(), false)
                .map(Object::toString)
                .toList();
        Assertions.assertIterableEquals(expectedValues, strings);
    }

}
