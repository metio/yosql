/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.internals.jdk;

import java.util.List;
import java.util.Locale;

/**
 * Lists all supported locales within YoSQL.
 */
public class SupportedLocales {

    public static final Locale ENGLISH = Locale.ENGLISH;
    public static final Locale GERMAN = Locale.GERMAN;
    public static final List<Locale> ALL = List.of(ENGLISH, GERMAN);

    public static Locale defaultLocale() {
        return ALL.stream()
                .filter(Locale.getDefault()::equals)
                .findFirst()
                .orElse(Locale.ENGLISH);
    }

}
