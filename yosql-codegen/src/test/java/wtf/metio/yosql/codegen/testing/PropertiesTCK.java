/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.testing;

import ch.qos.cal10n.verifier.Cal10nError;
import ch.qos.cal10n.verifier.IMessageKeyVerifier;
import ch.qos.cal10n.verifier.MessageKeyVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Verifies .properties files.
 *
 * @param <ENUMERATION>
 *            The type of the enumeration to test.
 */
public interface PropertiesTCK<ENUMERATION extends Enum<ENUMERATION>> {

    Logger LOGGER = LoggerFactory.getLogger(PropertiesTCK.class);

    /**
     * @return The class of the enum to test.
     */
    Class<ENUMERATION> getEnumClass();

    @Test
    @DisplayName("all properties exist in all locales")
    default void shouldDeclareAllPropertiesInAllSupportedLocales() {
        final IMessageKeyVerifier mkv = new MessageKeyVerifier(getEnumClass());
        final List<Cal10nError> errorList = mkv.verifyAllLocales();
        for (final Cal10nError error : errorList) {
            LOGGER.error("Properties validation failed: {}", error);
        }
        Assertions.assertEquals(0, errorList.size());
    }

}
