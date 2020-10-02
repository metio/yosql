/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.i18n;

import ch.qos.cal10n.MessageConveyor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.model.descriptions.GeneralOptionsDescriptions;

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
        final var text = translator.localized(GeneralOptionsDescriptions.LOCALE_DESCRIPTION);

        // then
        assertEquals("The locale to use.", text);
    }

}