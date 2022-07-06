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

    Function<String, Optional<Map<String, Object>>> findCompanyByName();

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
            findCompanyByName().apply("two").ifPresentOrElse(ReadingTests::print,
                    () -> LOG.log(System.Logger.Level.INFO, "Could not find company named 'two'"));
            findCompanies().apply(2, 5).forEach(ReadingTests::print);
            try {
                findCompanyByName().apply("three").ifPresent(company ->
                        LOG.log(System.Logger.Level.ERROR, "could read multiple companies with the same name"));
            } catch (final IllegalStateException exception) {
                LOG.log(System.Logger.Level.INFO, "Detected multiple companies with the same name");
            }

            findPerson().apply("alice").forEach(ReadingTests::print);
            try (final var persons = findPersons().get()) {
                persons.forEach(ReadingTests::print);
            }

            findItemByAllNames().apply("Android 49").forEach(result -> LOG.log(System.Logger.Level.INFO, result));
            findItemByName().apply("iPhone 47 eXtreme").forEach(result -> LOG.log(System.Logger.Level.INFO, result));

            queryAllUsers().get().forEach(ReadingTests::print);
            querySpecialUserWithConstantId().get().ifPresentOrElse(ReadingTests::print,
                    () -> LOG.log(System.Logger.Level.INFO, "Could not find user with constant ID"));
        } catch (final RuntimeException exception) {
            LOG.log(System.Logger.Level.ERROR, "Error while running READING tests", exception);
            System.exit(1);
        }
    }

    private static void print(final Map<String, Object> result) {
        LOG.log(System.Logger.Level.INFO, result);
    }

}
