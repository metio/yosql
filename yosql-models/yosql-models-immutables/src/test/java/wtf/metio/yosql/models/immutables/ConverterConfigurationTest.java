/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.immutables;

import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.configuration.ResultRowConverter;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ConverterConfigurationTest {

    @Test
    void shouldHaveNoDuplicatesInDefaultConfig() {
        final var converter = ConverterConfiguration.builder().build();
        final var counts = converter.uniqueConverterAliases();
        final var duplicates = counts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        assertTrue(duplicates.isEmpty());
    }

    @Test
    void shouldDetectDuplicatesInCustomConfig() {
        final var converter = ConverterConfiguration.builder()
                .addRowConverters(ResultRowConverter.builder()
                        .setAlias("alias")
                        .build())
                .addRowConverters(ResultRowConverter.builder()
                        .setAlias("alias")
                        .build())
                .build();
        final var counts = converter.uniqueConverterAliases();
        final var duplicates = counts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        assertFalse(duplicates.isEmpty());
        assertIterableEquals(Set.of("alias"), duplicates);
    }

}
