/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.dagger;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Default configuration for {@link Locale}s.
 */
@Module
@SuppressWarnings("static-method")
public class DefaultLocaleModule {

    @Localized
    @Provides
    @Singleton
    Locale provideUserLocale() {
        return Locale.ENGLISH;
    }

    @NonLocalized
    @Provides
    @Singleton
    Locale provideSystemLocale() {
        return Locale.ENGLISH;
    }

}
