/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public final class TestIterables {

    public static void assertIterable(final Iterable<?> values, final String... expectedValues) {
        Assertions.assertAll(mustEqual(values, expectedValues));
    }

    private static Executable[] mustEqual(final Iterable<?> values, final String[] expectedValues) {
        final var userInputs =
                StreamSupport.stream(values.spliterator(), false)
                        .collect(Collectors.toList());
        return IntStream.range(0, expectedValues.length)
            .mapToObj(index -> asExecutable(index, userInputs, expectedValues))
            .toArray(Executable[]::new);
    }

    private static Executable asExecutable(final int index, final List<?> userInputs, final String[] expectedValues) {
        if (index < expectedValues.length) {
            return () -> Assertions.assertEquals(expectedValues[index], userInputs.get(index).toString());
        } else {
            return () -> Assertions.fail("No expected value for:\n" + userInputs.get(index).toString());
        }
    }

}
