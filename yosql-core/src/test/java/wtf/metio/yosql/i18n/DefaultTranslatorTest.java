package wtf.metio.yosql.i18n;

import ch.qos.cal10n.MessageConveyor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.model.descriptions.GeneralOptionDescriptions;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DefaultTranslator")
class DefaultTranslatorTest {

    @Test
    @DisplayName("localizes enums")
    void shouldProvideLocalization() {
        // given
        final var messages = new MessageConveyor(Locale.ENGLISH);
        final var translator = new DefaultTranslator(messages, messages);

        // when
        final var text = translator.localized(GeneralOptionDescriptions.LOCALE_DESCRIPTION);

        // then
        assertEquals("The locale to use.", text);
    }

}