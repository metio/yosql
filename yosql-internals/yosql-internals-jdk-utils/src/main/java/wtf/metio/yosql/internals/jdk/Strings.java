/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.jdk;

import java.util.Locale;

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

    /**
     * Turn the first letter of the given value into its upper case version and append the rest of the value.
     *
     * @param value The value to transform.
     * @return The transformed value.
     */
    public static String upperCase(final String value) {
        return value.substring(0, 1).toUpperCase(Locale.ROOT) + value.substring(1);
    }

    /**
     * Turns all characters of a String into their lower case version.
     *
     * @param value The value to transform.
     * @return The transformed value.
     */
    public static String lowerCase(final String value) {
        return value.toLowerCase(Locale.ROOT);
    }

    /**
     * Converts camelCase to kebab-case.
     *
     * @param camelCase The original camelCase value.
     * @return The transformed kebab-case value.
     */
    public static String kebabCase(final String camelCase) {
        final var regex = "([a-z])([A-Z]+)";
        final var replacement = "$1-$2";
        return camelCase.replaceAll(regex, replacement).toLowerCase(Locale.ROOT);
    }

    /**
     * Replaces the last occurrence of a substring within a string with another string.
     *
     * @param original    The original string to work on.
     * @param toReplace   The substring to replace.
     * @param replacement The replacement for the substring.
     * @return The changed string.
     */
    public static String replaceLast(
            final String original,
            final String toReplace,
            final String replacement) {
        int pos = original.lastIndexOf(toReplace);
        if (pos > -1) {
            return original.substring(0, pos)
                    + replacement
                    + original.substring(pos + toReplace.length());
        }
        return original;
    }

    private Strings() {
        // utility class
    }

}
