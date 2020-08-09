package wtf.metio.yosql.i18n;

import ch.qos.cal10n.MessageConveyor;

import java.util.Locale;

/**
 * Object mother for i18n related classes
 */
public final class I18nObjectMother {

    public static Translator testTranslator() {
        final var messages = new MessageConveyor(Locale.ENGLISH);
        return new DefaultTranslator(messages, messages);
    }

    private I18nObjectMother() {
        // factory class
    }

}
