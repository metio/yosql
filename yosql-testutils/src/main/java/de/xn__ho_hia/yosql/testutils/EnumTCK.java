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
    @SuppressWarnings("nls")
    default Stream<String> invalidValues() {
        return Stream.of("abc", "123");
    }

    /**
     * @return Tests that verify that enum instances can be created from valid values.
     */
    @TestFactory
    @SuppressWarnings("nls")
    default Stream<DynamicTest> shouldCreateEnumForValidValue() {
        return validValues()
                .map(value -> dynamicTest(String.format("should create [%s] from [%s]", getEnumClass(), value),
                        () -> Assertions.assertNotNull(Enum.valueOf(getEnumClass(), value))));
    }

    /**
     * @return Tests that verify that no enum instance can be created from invalid values.
     */
    @TestFactory
    @SuppressWarnings("nls")
    default Stream<DynamicTest> shouldNotCreateEnumForInvalidValue() {
        return invalidValues()
                .map(value -> dynamicTest(String.format("should create [%s] from [%s]", getEnumClass(), value),
                        () -> Assertions.assertThrows(IllegalArgumentException.class,
                                () -> Enum.valueOf(getEnumClass(), value))));
    }

}
