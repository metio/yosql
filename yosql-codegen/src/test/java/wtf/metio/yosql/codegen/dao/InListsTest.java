/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("InLists")
class InListsTest {

    private static SqlParameter parameter(final String name, final String type, final int... indices) {
        return SqlParameter.builder()
                .setName(name)
                .setType(type)
                .setIndices(indices)
                .build();
    }

    private static SqlConfiguration configuration(final SqlParameter... parameters) {
        return SqlConfiguration.builder()
                .setName("findTenants")
                .addParameters(parameters)
                .build();
    }

    @Nested
    @DisplayName("what needs a placeholder of its own")
    class Expands {

        @Test
        @DisplayName("every collection a driver will not take as one value")
        void collections() {
            assertTrue(InLists.expands(parameter("ids", "java.util.List<java.util.UUID>", 1)));
            assertTrue(InLists.expands(parameter("ids", "java.util.Set<java.lang.String>", 1)));
            assertTrue(InLists.expands(parameter("ids", "java.util.Collection<java.lang.Long>", 1)));
            assertTrue(InLists.expands(parameter("ids", "List<java.util.UUID>", 1)));
        }

        @Test
        @DisplayName("nothing that is one value")
        void singleValues() {
            assertFalse(InLists.expands(parameter("id", "java.util.UUID", 1)));
            assertFalse(InLists.expands(parameter("id", "java.lang.String", 1)));
        }

        @Test
        @DisplayName("not an array, which is how a batch passes one value per execution")
        void arrays() {
            assertFalse(InLists.expands(parameter("ids", "java.util.UUID[]", 1)));
        }

        @Test
        @DisplayName("a collection is compared against its column one value at a time")
        void elementType() {
            assertEquals("java.util.UUID",
                    InLists.elementType(parameter("ids", "java.util.List<java.util.UUID>", 1))
                            .orElseThrow().toString());
            assertEquals("java.util.UUID",
                    InLists.elementType(parameter("id", "java.util.UUID", 1)).orElseThrow().toString());
        }

    }

    @Nested
    @DisplayName("the query around the collections")
    class Parts {

        @Test
        @DisplayName("splits where the values go and leaves every other placeholder in place")
        void splitsAtTheCollection() {
            final var parts = InLists.queryParts(
                    configuration(parameter("ids", "java.util.List<java.util.UUID>", 1),
                            parameter("accountId", "java.util.UUID", 2)),
                    "select id from tenant where id in (:ids) and account_id = :accountId");
            assertEquals(List.of(
                            "select id from tenant where id in (",
                            ") and account_id = ?"),
                    parts);
        }

        @Test
        @DisplayName("one part more than there are collections, so joining rebuilds the query")
        void splitsAtEveryCollection() {
            final var parts = InLists.queryParts(
                    configuration(parameter("ids", "java.util.List<java.util.UUID>", 1),
                            parameter("slugs", "java.util.List<java.lang.String>", 2)),
                    "select id from tenant where id in (:ids) and slug in (:slugs)");
            assertEquals(3, parts.size());
            assertEquals("select id from tenant where id in (", parts.get(0));
            assertEquals(") and slug in (", parts.get(1));
            assertEquals(")", parts.get(2));
        }

        @Test
        @DisplayName("a question mark the author wrote is text, not a placeholder")
        void leavesLiteralsAlone() {
            final var parts = InLists.queryParts(
                    configuration(parameter("ids", "java.util.List<java.util.UUID>", 1)),
                    "select id from tenant where name <> 'why?' and id in (:ids)");
            assertEquals(List.of("select id from tenant where name <> 'why?' and id in (", ")"), parts);
        }

    }

    @Nested
    @DisplayName("the order values are bound in")
    class Order {

        @Test
        @DisplayName("follows the statement rather than the declaration")
        void followsTheStatement() {
            final var order = InLists.placeholderOrder(configuration(
                    parameter("accountId", "java.util.UUID", 2),
                    parameter("ids", "java.util.List<java.util.UUID>", 1)));
            assertEquals(List.of("ids", "accountId"),
                    order.stream().map(parameter -> parameter.name().orElseThrow()).toList());
        }

        @Test
        @DisplayName("binds a name used twice twice")
        void repeatsAName() {
            final var order = InLists.placeholderOrder(configuration(
                    parameter("id", "java.util.UUID", 1, 3),
                    parameter("slug", "java.lang.String", 2)));
            assertEquals(List.of("id", "slug", "id"),
                    order.stream().map(parameter -> parameter.name().orElseThrow()).toList());
        }

    }

    @Nested
    @DisplayName("the empty case")
    class Negation {

        @Test
        @DisplayName("is only a problem where the list is negated")
        void findsNegation() {
            assertTrue(InLists.isNegated("select id from tenant where id not in (:ids)", "ids"));
            assertTrue(InLists.isNegated("select id from tenant where id NOT  IN ( :ids )", "ids"));
            assertFalse(InLists.isNegated("select id from tenant where id in (:ids)", "ids"));
        }

        @Test
        @DisplayName("names the collection it is asked about, not another one")
        void distinguishesCollections() {
            final var sql = "select id from tenant where id in (:ids) and slug not in (:slugs)";
            assertFalse(InLists.isNegated(sql, "ids"));
            assertTrue(InLists.isNegated(sql, "slugs"));
        }

    }

}
