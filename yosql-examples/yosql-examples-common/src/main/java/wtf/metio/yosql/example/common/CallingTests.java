/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.example.common;

import org.immutables.value.Value;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Value.Immutable
public interface CallingTests {

    System.Logger LOG = System.getLogger(CallingTests.class.getName());

    static ImmutableCallingTests.CallRandomNumberBuildStage builder() {
        return ImmutableCallingTests.builder();
    }

    Supplier<Optional<Map<String, Object>>> callRandomNumber();

    Function<String, Optional<Map<String, Object>>> callNextPrime();

    Supplier<Optional<Map<String, Object>>> callNames();

    @Value.Lazy
    default void runCallingTests() {
        try {
            callRandomNumber().get().ifPresentOrElse(CallingTests::printFirstValue,
                    () -> LOG.log(System.Logger.Level.INFO, "Could get random number"));
            callNextPrime().apply("20").ifPresentOrElse(CallingTests::printFirstValue,
                    () -> LOG.log(System.Logger.Level.INFO, "Could get names"));
            callNames().get().ifPresentOrElse(CallingTests::printFirstValue,
                    () -> LOG.log(System.Logger.Level.INFO, "Could get names"));
        } catch (final RuntimeException exception) {
            LOG.log(System.Logger.Level.ERROR, "Error while running CALLING tests", exception);
            System.exit(1);
        }
    }

    private static void printFirstValue(final Map<String, Object> result) {
        LOG.log(System.Logger.Level.INFO, result.values().stream().findFirst().orElseThrow());
    }

}
