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
            queryAllCompanies().get().forEach(System.out::println);
            findCompanyByName().apply("two").forEach(System.out::println);
            findCompanies().apply(10, 20).forEach(System.out::println);

            findPerson().apply("alice").forEach(System.out::println);
            try (final var persons = findPersons().get()) {
                persons.forEach(System.out::println);
            }

            findItemByAllNames().apply("").forEach(System.out::println);
            findItemByName().apply("").forEach(System.out::println);

            queryAllUsers().get().forEach(System.out::println);
            querySpecialUserWithConstantId().get()
                    .ifPresentOrElse(System.out::println,
                            () -> LOG.log(System.Logger.Level.INFO, "Could not find user with constant ID"));
        } catch (final RuntimeException exception) {
            LOG.log(System.Logger.Level.ERROR, "Error while running READING tests", exception);
            System.exit(1);
        }
    }

}
