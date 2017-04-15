package de.xn__ho_hia.yosql.dagger;

import java.util.Locale;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import dagger.Module;
import dagger.Provides;

/**
 * I18N module
 */
@Module
@SuppressWarnings("static-method")
public class I18nModule {

    @Provides
    IMessageConveyor provideIMessageConveyor() {
        return new MessageConveyor(Locale.ENGLISH);
    }

}
