/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.internals.jdk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Strings")
class StringsTest {

    @Test
    void isBlank() {
        assertAll(
                () -> assertAll("blank",
                        () -> assertTrue(Strings.isBlank(null)),
                        () -> assertTrue(Strings.isBlank("")),
                        () -> assertTrue(Strings.isBlank(" "))),
                () -> assertAll("non-blank",
                        () -> assertFalse(Strings.isBlank("a"))));
    }

    @Test
    void upperCase() {
        assertAll(
                () -> assertEquals(" ", Strings.upperCase(" ")),
                () -> assertEquals("A", Strings.upperCase("a")),
                () -> assertEquals("Abc", Strings.upperCase("abc")),
                () -> assertEquals("Abc", Strings.upperCase("Abc")),
                () -> assertEquals("Äbc", Strings.upperCase("äbc")));
    }

    @Test
    void lowerCase() {
        assertAll(
                () -> assertEquals(" ", Strings.lowerCase(" ")),
                () -> assertEquals("a", Strings.lowerCase("A")),
                () -> assertEquals("abc", Strings.lowerCase("Abc")),
                () -> assertEquals("abc", Strings.lowerCase("abc")),
                () -> assertEquals("äbc", Strings.lowerCase("Äbc")));
    }

    @Test
    void kebabCase() {
        assertAll(
                () -> assertEquals("", Strings.kebabCase("")),
                () -> assertEquals(" ", Strings.kebabCase(" ")),
                () -> assertEquals("a", Strings.kebabCase("A")),
                () -> assertEquals("abc", Strings.kebabCase("Abc")),
                () -> assertEquals("abc", Strings.kebabCase("abc")),
                () -> assertEquals("äbc", Strings.kebabCase("Äbc")),
                () -> assertEquals("camel-case", Strings.kebabCase("CamelCase")),
                () -> assertEquals("camel-case-other", Strings.kebabCase("CamelCaseOther")),
                () -> assertEquals("camel-case-other-more", Strings.kebabCase("CamelCaseOtherMore")));
    }

}
