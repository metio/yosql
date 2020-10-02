/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dagger;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.Locale;

/**
 * Default configuration for {@link Locale}s.
 */
@Module
// TODO: move to OrchestrationModule
public final class DefaultLocaleModule {

    @Provides
    @Singleton
    Locale provideSystemLocale() {
        return Locale.ENGLISH;
    }

}
