/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.immutables;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@DisplayName("NamesConfiguration")
class NamesConfigurationTest {

    @Test
    void shouldHaveNoDuplicatesInDefaultConfig() {
        final var names = NamesConfiguration.builder().build();
        final var counts = names.uniqueValueCount();
        final var duplicates = counts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        Assertions.assertTrue(duplicates.isEmpty());
    }

    @Test
    void shouldDetectDuplicatesInCustomConfig() {
        final var names = NamesConfiguration.builder()
                .setAction("action")
                .setBatch("action")
                .build();
        final var counts = names.uniqueValueCount();
        final var duplicates = counts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        Assertions.assertFalse(duplicates.isEmpty());
        Assertions.assertIterableEquals(Set.of("action"), duplicates);
    }

}
