/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.testing;

import ch.qos.cal10n.verifier.MessageKeyVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Verifies .properties files.
 *
 * @param <ENUMERATION> The type of the enumeration to test.
 */
public abstract class PropertiesTCK<ENUMERATION extends Enum<ENUMERATION>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesTCK.class);

    private final Class<ENUMERATION> enumClass;

    protected PropertiesTCK(final Class<ENUMERATION> enumClass) {
        this.enumClass = enumClass;
    }

    @Test
    @DisplayName("all properties exist in all locales")
    final void shouldDeclareAllPropertiesInAllSupportedLocales() {
        final var mkv = new MessageKeyVerifier(enumClass);
        final var errorList = mkv.verifyAllLocales();
        for (final var error : errorList) {
            LOGGER.error("Properties validation failed: {}", error);
        }
        Assertions.assertEquals(0, errorList.size());
    }

}
