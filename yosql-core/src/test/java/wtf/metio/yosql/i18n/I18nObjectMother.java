/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.i18n;

import ch.qos.cal10n.MessageConveyor;

import java.util.Locale;

/**
 * Object mother for i18n related classes
 */
public final class I18nObjectMother {

    public static Translator testTranslator() {
        final var messages = new MessageConveyor(Locale.ENGLISH);
        return new DefaultTranslator(messages);
    }

    private I18nObjectMother() {
        // factory class
    }

}
