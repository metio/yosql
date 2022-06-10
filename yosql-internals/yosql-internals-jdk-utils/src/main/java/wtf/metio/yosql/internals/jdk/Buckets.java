/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
     * @return A filter to use with @{@link Stream#filter(Predicate)}.
     * @param <T> The type of the object in the stream.
     */
    public static <T> Predicate<T> distinctByKey(final Function<? super T, ?> keyExtractor) {
        final var seen = ConcurrentHashMap.newKeySet();
        return object -> seen.add(keyExtractor.apply(object));
    }

    private Buckets() {
        // utility class
    }

}
