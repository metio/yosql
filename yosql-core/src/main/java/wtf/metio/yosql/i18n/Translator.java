package wtf.metio.yosql.i18n;

/**
 * Translates the descriptions of several options.
 */
public interface Translator {

    /**
     * @param key The key to use.
     * @return The resulting localized message.
     */
    <E extends Enum<E>> String localized(E key);

    /**
     * @param key       The key to use.
     * @param arguments The arguments to apply.
     * @return The resulting system message.
     */
    <E extends Enum<E>> String nonLocalized(E key, Object... arguments);

}
