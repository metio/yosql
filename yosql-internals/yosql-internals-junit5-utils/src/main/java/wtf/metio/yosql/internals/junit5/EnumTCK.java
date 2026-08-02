/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.internals.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

/**
 * TCK for enumerations.
 *
 * @param <ENUMERATION> The enumeration type.
 */
public interface EnumTCK<ENUMERATION extends Enum<ENUMERATION>> {

    /**
     * @return The class of the enum to test.
     */
    Class<ENUMERATION> getEnumClass();

    /**
     * @return All valid enum values for the given enum class.
     */
    Stream<String> validValues();

    /**
     * @return Any invalid enum values for the given enum class.
     */
    default Stream<String> invalidValues() {
        return Stream.of();
    }

    /**
     * @return Tests that verify that enum instances can be created from valid values.
     */
    @TestFactory
    @DisplayName("create enum for valid value")
    default Stream<DynamicTest> shouldCreateEnumForValidValue() {
        return validValues()
                .map(value -> dynamicTest("should create [%s] from [%s]".formatted(getEnumClass().getSimpleName(), value),
                        () -> Assertions.assertNotNull(Enum.valueOf(getEnumClass(), value))));
    }

    /**
     * @return Tests that verify that no enum instance can be created from invalid values.
     */
    @TestFactory
    @DisplayName("reject invalid values")
    default Stream<DynamicTest> shouldNotCreateEnumForInvalidValue() {
        return invalidValues()
                .map(value -> dynamicTest("should not create [%s] from [%s]".formatted(getEnumClass().getSimpleName(), value),
                        () -> Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Enum.valueOf(getEnumClass(), value))));
    }

    /**
     * @return Tests that verify that all enum values are verified.
     */
    @TestFactory
    @DisplayName("all enum values are checked")
    default Stream<DynamicTest> shouldVerifyAllValues() {
        return Stream.of(dynamicTest("should verify all values of [%s]".formatted(getEnumClass().getSimpleName()),
                () -> Assertions.assertEquals(validValues().count(), getEnumClass().getEnumConstants().length)));
    }

}
