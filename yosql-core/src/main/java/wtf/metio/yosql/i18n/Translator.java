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
// TODO: remove & replace with IMessageConveyor
public interface Translator {

    /**
     * @param key       The key to use.
     * @param arguments The arguments to apply.
     * @return The resulting system message.
     */
    <E extends Enum<E>> String localized(E key, Object... arguments);

}
