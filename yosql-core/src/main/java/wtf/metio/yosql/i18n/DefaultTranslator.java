/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.i18n;

import ch.qos.cal10n.IMessageConveyor;

final class DefaultTranslator implements Translator {

    private final IMessageConveyor nonLocalizedMessages;

    DefaultTranslator(final IMessageConveyor nonLocalizedMessages) {
        this.nonLocalizedMessages = nonLocalizedMessages;
    }

    @Override
    public <E extends Enum<E>> String localized(final E key, final Object... arguments) {
        return nonLocalizedMessages.getMessage(key, arguments);
    }

}
