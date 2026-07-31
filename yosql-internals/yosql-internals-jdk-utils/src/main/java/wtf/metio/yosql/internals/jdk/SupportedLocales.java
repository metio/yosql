/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.internals.jdk;

import java.util.List;
import java.util.Locale;

/**
 * Lists all supported locales within YoSQL.
 */
public final class SupportedLocales {

    public static final Locale ENGLISH = Locale.ENGLISH;
    public static final Locale GERMAN = Locale.GERMAN;
    public static final List<Locale> ALL = List.of(ENGLISH, GERMAN);

    public static Locale defaultLocale() {
        return ALL.stream()
                .filter(Locale.getDefault()::equals)
                .findFirst()
                .orElse(Locale.ENGLISH);
    }

    private SupportedLocales() {
        // utility class
    }

}
