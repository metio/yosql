/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.i18n;

/**
 * Translates the descriptions of several options.
 */
public interface Translator {

    /**
     * @param key The key to use.
     * @return The resulting localized message.
     */
    // TODO: remove?
    @Deprecated
    <E extends Enum<E>> String localized(E key);

    /**
     * @param key       The key to use.
     * @param arguments The arguments to apply.
     * @return The resulting system message.
     */
    // TODO: rename to localized?
    <E extends Enum<E>> String nonLocalized(E key, Object... arguments);

}
