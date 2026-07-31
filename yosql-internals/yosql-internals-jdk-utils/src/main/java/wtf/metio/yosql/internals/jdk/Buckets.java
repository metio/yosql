/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.internals.jdk;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Utility methods that handle {@link Collection collections} and {@link Stream streams}. The class is called "Buckets"
 * because 'Collections' is way too common.
 */
public final class Buckets {

    /**
     * @param collection The collection to check.
     * @return true if value is not null and not empty, false otherwise.
     */
    public static boolean hasEntries(final Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * Allows to use {@link Stream#distinct()} with a key.
     *
     * @param keyExtractor The function to extract the key from an object.
     * @param <T>          The type of the object in the stream.
     * @return A filter to use with @{@link Stream#filter(Predicate)}.
     */
    public static <T> Predicate<T> distinctByKey(final Function<? super T, ?> keyExtractor) {
        final var seen = ConcurrentHashMap.newKeySet();
        return object -> seen.add(keyExtractor.apply(object));
    }

    private Buckets() {
        // utility class
    }

}
