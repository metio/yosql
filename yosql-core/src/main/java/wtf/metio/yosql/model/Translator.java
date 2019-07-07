/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.model;

import ch.qos.cal10n.IMessageConveyor;

/**
 * Wrapper around {@link IMessageConveyor}.
 */
public class Translator {

    private final IMessageConveyor localizedMessages;
    private final IMessageConveyor nonLocalizedMessages;

    /**
     * @param localizedMessages
     *            The localized message conveyor to use.
     * @param nonLocalizedMessages
     *            The non-localized message conveyor to use.
     */
    public Translator(final IMessageConveyor localizedMessages, final IMessageConveyor nonLocalizedMessages) {
        this.localizedMessages = localizedMessages;
        this.nonLocalizedMessages = nonLocalizedMessages;
    }

    /**
     * @param key
     *            The key to use.
     * @return The resulting localized message.
     */
    public String localized(final GenerateOptionDescriptions key) {
        return localizedMessages.getMessage(key);
    }

    /**
     * @param key
     *            The key to use.
     * @return The resulting localized message.
     */
    public String localized(final HelpOptionDescriptions key) {
        return localizedMessages.getMessage(key);
    }

    /**
     * @param key
     *            The key to use.
     * @return The resulting localized message.
     */
    public String localized(final GeneralOptionDescriptions key) {
        return localizedMessages.getMessage(key);
    }

    /**
     * @param key
     *            The key to use.
     * @param arguments
     *            The arguments to apply.
     * @return The resulting system message.
     */
    public <E extends Enum<E>> String nonLocalized(final E key, final Object... arguments) {
        return nonLocalizedMessages.getMessage(key, arguments);
    }

}
