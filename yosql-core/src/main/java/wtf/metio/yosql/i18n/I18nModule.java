/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.i18n;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.Locale;

/**
 * I18N module
 */
@Module
// TODO: move to orchestration module
public class I18nModule {

    @Provides
    @Singleton
    // TODO: remove Translator interface & replace with IMessageConveyor
    Translator provideTranslator(final IMessageConveyor systemMessages) {
        return new DefaultTranslator(systemMessages);
    }

    @Provides
    @Singleton
    IMessageConveyor provideIMessageConveyor(final Locale systemLocale) {
        return new MessageConveyor(systemLocale);
    }

}
