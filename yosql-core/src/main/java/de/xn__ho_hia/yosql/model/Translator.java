package de.xn__ho_hia.yosql.model;

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
