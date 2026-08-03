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

    /**
     * Matched on language alone: the bundles are per language, and a reader whose locale is
     * {@code de_DE} or {@code de_AT} wants the German one. Comparing whole locales gave them
     * English, because neither equals {@link Locale#GERMAN}.
     */
    public static Locale defaultLocale() {
        final var language = Locale.getDefault().getLanguage();
        return ALL.stream()
                .filter(supported -> supported.getLanguage().equals(language))
                .findFirst()
                .orElse(Locale.ENGLISH);
    }

    private SupportedLocales() {
        // utility class
    }

}
