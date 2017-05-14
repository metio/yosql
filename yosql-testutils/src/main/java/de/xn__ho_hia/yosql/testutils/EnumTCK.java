/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.testutils;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

/**
 * TCK for enumerations.
 *
 * @param <ENUMERATION>
 *            The enumeration type.
 */
@SuppressWarnings("nls")
public interface EnumTCK<ENUMERATION extends Enum<ENUMERATION>> {

    /**
     * @return The class of the enum to test.
     */
    Class<ENUMERATION> getEnumClass();

    /**
     * @return Valid enum values for the given enum class.
     */
    Stream<String> validValues();

    /**
     * @return Invalid enum values for the given enum class.
     */
    default Stream<String> invalidValues() {
        return Stream.of();
    }

    /**
     * @return Tests that verify that enum instances can be created from valid values.
     */
    @TestFactory
    default Stream<DynamicTest> shouldCreateEnumForValidValue() {
        return validValues()
                .map(value -> dynamicTest(String.format("should create [%s] from [%s]", getEnumClass(), value),
                        () -> Assertions.assertNotNull(Enum.valueOf(getEnumClass(), value))));
    }

    /**
     * @return Tests that verify that no enum instance can be created from invalid values.
     */
    @TestFactory
    default Stream<DynamicTest> shouldNotCreateEnumForInvalidValue() {
        return invalidValues()
                .map(value -> dynamicTest(String.format("should not create [%s] from [%s]", getEnumClass(), value),
                        () -> Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Enum.valueOf(getEnumClass(), value))));
    }

    /**
     * @return Tests that verify that all enum values are verified.
     */
    @TestFactory
    default Stream<DynamicTest> shouldVerifyAllValues() {
        return Stream.of(dynamicTest(String.format("should verify all values of [%s]", getEnumClass()),
                () -> Assertions.assertEquals(validValues().count(), getEnumClass().getEnumConstants().length)));
    }

}
