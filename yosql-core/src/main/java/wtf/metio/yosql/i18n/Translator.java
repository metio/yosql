package wtf.metio.yosql.i18n;

import wtf.metio.yosql.model.descriptions.GeneralOptionDescriptions;
import wtf.metio.yosql.model.descriptions.GenerateOptionDescriptions;
import wtf.metio.yosql.model.descriptions.HelpOptionDescriptions;
import wtf.metio.yosql.model.options.JdbcNamesOptions;

/**
 * Translates the descriptions of several options.
 */
public interface Translator {

    /**
     * @param key The key to use.
     * @return The resulting localized message.
     */
    @Deprecated
    String localized(GenerateOptionDescriptions key);

    /**
     * @param key The key to use.
     * @return The resulting localized message.
     */
    @Deprecated
    String localized(HelpOptionDescriptions key);

    /**
     * @param key The key to use.
     * @return The resulting localized message.
     */
    @Deprecated
    String localized(GeneralOptionDescriptions key);

    /**
     * @param key The key to use.
     * @return The resulting localized message.
     */
    <E extends Enum<E>> String localizedMessage(E key);

    /**
     * @param key       The key to use.
     * @param arguments The arguments to apply.
     * @return The resulting system message.
     */
    <E extends Enum<E>> String nonLocalized(E key, Object... arguments);

}
