/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.utils;

/**
 * Utility methods that handle {@link String Strings}.
 */
public final class Strings {

    /**
     * @param value The value to check.
     * @return true if value is blank, false otherwise.
     */
    public static boolean isBlank(final String value) {
        return value == null || value.isBlank();
    }

    private Strings() {
        // utility class
    }

}
