/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
            // The writing tests put six companies, six persons, four users and three items in.
            // Reading them back is what proves the generated code round-trips, which printing the
            // rows and hoping somebody looks at them never did.
            Verify.equal(6, queryAllCompanies().get().size(), "companies read back");

            final var two = findCompanyByName().apply("two");
            Verify.that(two.isPresent(), "company 'two' found");
            Verify.equal("two", two.orElseThrow().get("NAME"), "the company found is the one asked for");

            // Two companies are called 'three', and a statement returning one row is supposed to
            // refuse rather than pick one.
            Verify.throwsException(() -> findCompanyByName().apply("three"),
                    "reading one row from a query that finds several");

            Verify.equal(1, findPerson().apply("alice").size(), "person 'alice' read back");
            try (final var persons = findPersons().get()) {
                Verify.equal(6L, persons.count(), "persons read back through a cursor");
            }

            Verify.equal(1, findItemByAllNames().apply("Android 49").size(), "item read through its converter");
            Verify.equal(1, findItemByName().apply("iPhone 47 eXtreme").size(), "item read by name");

            Verify.equal(4, queryAllUsers().get().size(), "users read back");
            Verify.that(querySpecialUserWithConstantId().get().isPresent(),
                    "the user a constant in the statement selects");
        } catch (final RuntimeException exception) {
            LOG.log(System.Logger.Level.ERROR, "Error while running READING tests", exception);
            System.exit(1);
        }
    }

}
