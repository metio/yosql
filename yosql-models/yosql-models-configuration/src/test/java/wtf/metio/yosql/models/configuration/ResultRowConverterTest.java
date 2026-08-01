/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResultRowConverter")
class ResultRowConverterTest {

    @Test
    @DisplayName("names the class and leaves the rest to be resolved from it")
    void fromClassName() {
        final var converter = ResultRowConverter.fromClassName("com.example.MyConverter");

        assertNotNull(converter);
        assertAll(
                () -> assertEquals("com.example.MyConverter", converter.converterType().orElseThrow()),
                () -> assertTrue(converter.alias().isEmpty()),
                () -> assertTrue(converter.methodName().isEmpty()),
                () -> assertTrue(converter.resultType().isEmpty()));
    }

    @Test
    @DisplayName("surrounding whitespace is not part of the class name")
    void stripsTheClassName() {
        assertEquals("com.example.MyConverter",
                ResultRowConverter.fromClassName("  com.example.MyConverter  ").converterType().orElseThrow());
    }

    @Test
    @DisplayName("naming nothing is naming no converter")
    void namesNothing() {
        assertAll(
                () -> assertNull(ResultRowConverter.fromClassName(null)),
                () -> assertNull(ResultRowConverter.fromClassName("")),
                () -> assertNull(ResultRowConverter.fromClassName("   ")));
    }

}
