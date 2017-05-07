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
