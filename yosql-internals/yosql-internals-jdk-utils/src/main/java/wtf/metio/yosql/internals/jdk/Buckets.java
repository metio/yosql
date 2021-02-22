/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.jdk;

import java.util.Collection;

/**
 * Utility methods that handle {@link java.util.Collection Collections}. The class is called "Buckets" because
 * "Collections" is way too common.
 */
public final class Buckets {

    /**
     * @param collection The collection to check.
     * @return true if value is blank, false otherwise.
     */
    public static boolean hasEntries(final Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    private Buckets() {
        // utility class
    }

}
