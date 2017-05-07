package de.xn__ho_hia.yosql.dagger;

import java.util.Locale;

import javax.inject.Singleton;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.model.Translator;

/**
 * I18N module
 */
@Module
@SuppressWarnings("static-method")
public class I18nModule {

    @Provides
    @Singleton
    Translator provideTranslator(
            @Localized final IMessageConveyor userMessages,
            @NonLocalized final IMessageConveyor systemMessages) {
        return new Translator(userMessages, systemMessages);
    }

    @NonLocalized
    @Provides
    @Singleton
    IMessageConveyor provideNonLocalizedIMessageConveyor(@NonLocalized final Locale systemLocale) {
        return new MessageConveyor(systemLocale);
    }

    @Localized
    @Provides
    @Singleton
    IMessageConveyor provideLocalizedIMessageConveyor(@Localized final Locale userLocale) {
        return new MessageConveyor(userLocale);
    }

}
