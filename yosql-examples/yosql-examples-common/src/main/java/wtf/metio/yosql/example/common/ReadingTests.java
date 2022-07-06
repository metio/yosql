/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.example.common;

import org.immutables.value.Value;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Value.Immutable
public interface ReadingTests {

    System.Logger LOG = System.getLogger(ReadingTests.class.getName());

    static ImmutableReadingTests.QueryAllCompaniesBuildStage builder() {
        return ImmutableReadingTests.builder();
    }

    Supplier<List<Map<String, Object>>> queryAllCompanies();

    Function<String, List<Map<String, Object>>> findCompanyByName();

    BiFunction<Integer, Integer, List<Map<String, Object>>> findCompanies();

    Function<String, List<Map<String, Object>>> findPerson();

    Supplier<Stream<Map<String, Object>>> findPersons();

    Function<String, List<?>> findItemByAllNames();

    Function<String, List<?>> findItemByName();

    Supplier<List<Map<String, Object>>> queryAllUsers();

    Supplier<Optional<Map<String, Object>>> querySpecialUserWithConstantId();

    Supplier<List<Map<String, Object>>> queryAdminUser();

    @Value.Lazy
    default void runReadingTests() {
        try {
            queryAllCompanies().get().forEach(ReadingTests::print);
            findCompanyByName().apply("two").forEach(ReadingTests::print);
            findCompanies().apply(10, 20).forEach(ReadingTests::print);

            findPerson().apply("alice").forEach(ReadingTests::print);
            try (final var persons = findPersons().get()) {
                persons.forEach(ReadingTests::print);
            }

            findItemByAllNames().apply("").forEach(result -> LOG.log(System.Logger.Level.INFO, result));
            findItemByName().apply("").forEach(result -> LOG.log(System.Logger.Level.INFO, result));

            queryAllUsers().get().forEach(ReadingTests::print);
            querySpecialUserWithConstantId().get().ifPresentOrElse(ReadingTests::printFirstValue,
                    () -> LOG.log(System.Logger.Level.INFO, "Could not find user with constant ID"));
        } catch (final RuntimeException exception) {
            LOG.log(System.Logger.Level.ERROR, "Error while running READING tests", exception);
            System.exit(1);
        }
    }

    private static void print(final Map<String, Object> result) {
        LOG.log(System.Logger.Level.INFO, result);
    }

    private static void printFirstValue(final Map<String, Object> result) {
        LOG.log(System.Logger.Level.INFO, result.values().stream().findFirst().orElseThrow());
    }

}
