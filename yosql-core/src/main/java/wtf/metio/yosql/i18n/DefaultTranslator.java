/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.i18n;

import ch.qos.cal10n.IMessageConveyor;
import wtf.metio.yosql.model.descriptions.GeneralOptionDescriptions;
import wtf.metio.yosql.model.descriptions.GenerateOptionDescriptions;
import wtf.metio.yosql.model.descriptions.HelpOptionDescriptions;
import wtf.metio.yosql.model.options.JdbcNamesOptions;

final class DefaultTranslator implements Translator {

    private final IMessageConveyor localizedMessages;
    private final IMessageConveyor nonLocalizedMessages;

    DefaultTranslator(
            final IMessageConveyor localizedMessages,
            final IMessageConveyor nonLocalizedMessages) {
        this.localizedMessages = localizedMessages;
        this.nonLocalizedMessages = nonLocalizedMessages;
    }

    @Override
    public String localized(final GenerateOptionDescriptions key) {
        return localizedMessage(key);
    }

    @Override
    public String localized(final HelpOptionDescriptions key) {
        return localizedMessage(key);
    }

    @Override
    public String localized(final GeneralOptionDescriptions key) {
        return localizedMessage(key);
    }

    @Override
    public <E extends Enum<E>> String localizedMessage(final E key) {
        return localizedMessages.getMessage(key);
    }

    @Override
    public <E extends Enum<E>> String nonLocalized(final E key, final Object... arguments) {
        return nonLocalizedMessages.getMessage(key, arguments);
    }

}
