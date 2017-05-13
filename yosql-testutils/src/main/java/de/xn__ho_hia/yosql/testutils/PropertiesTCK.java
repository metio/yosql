/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.testutils;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.qos.cal10n.verifier.Cal10nError;
import ch.qos.cal10n.verifier.IMessageKeyVerifier;
import ch.qos.cal10n.verifier.MessageKeyVerifier;

/**
 * Verifies .properties files.
 *
 * @param <ENUMERATION>
 *            The type of the enumeration to test.
 */
public abstract class PropertiesTCK<ENUMERATION extends Enum<ENUMERATION>> {

    /**
     * @return The class of the enum to test.
     */
    protected abstract Class<ENUMERATION> getEnumClass();

    @Test
    void shouldDeclareAllPropertiesInAllSupportedLocales() {
        final IMessageKeyVerifier mkv = new MessageKeyVerifier(getEnumClass());
        final List<Cal10nError> errorList = mkv.verifyAllLocales();
        for (final Cal10nError error : errorList) {
            System.out.println(error);
        }
        Assertions.assertEquals(0, errorList.size());
    }

}
