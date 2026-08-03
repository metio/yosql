/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TableScope")
class TableScopeTest {

    @Nested
    @DisplayName("what it can name")
    class Known {

        @Test
        void shouldReadASingleTable() {
            final var scope = TableScope.of("select id, slug from tenant where id = :id");

            assertAll(
                    () -> assertTrue(scope.exhaustive()),
                    () -> assertIterableEquals(List.of("tenant"), scope.tables()));
        }

        @Test
        @DisplayName("a join puts both tables in scope")
        void shouldReadJoins() {
            final var scope = TableScope.of("""
                    select c.pid
                    from companies c
                             inner join departments d on c.pid = d.company_pid
                    where d.pid = :department""");

            assertAll(
                    () -> assertTrue(scope.exhaustive()),
                    () -> assertIterableEquals(List.of("companies", "departments"), scope.tables()));
        }

        @Test
        @DisplayName("an alias resolves to the table it stands for")
        void shouldResolveAliases() {
            final var scope = TableScope.of("select c.pid from companies c join departments d on c.pid = d.company_pid");

            assertAll(
                    () -> assertEquals("companies", scope.resolve("c").orElseThrow()),
                    () -> assertEquals("departments", scope.resolve("d").orElseThrow()),
                    () -> assertTrue(scope.resolve("nowhere").isEmpty()));
        }

        @Test
        @DisplayName("a table can be qualified by its own name")
        void shouldResolveTableName() {
            final var scope = TableScope.of("select tenant.id from tenant");

            assertEquals("tenant", scope.resolve("tenant").orElseThrow());
        }

        @Test
        void shouldReadWrites() {
            assertAll(
                    () -> assertIterableEquals(List.of("tenant"),
                            TableScope.of("insert into tenant (id) values (:id)").tables()),
                    () -> assertIterableEquals(List.of("tenant"),
                            TableScope.of("update tenant set slug = :slug where id = :id").tables()),
                    () -> assertIterableEquals(List.of("tenant"),
                            TableScope.of("delete from tenant where id = :id").tables()));
        }

    }

    @Nested
    @DisplayName("what it refuses to guess at")
    class Unknown {

        @Test
        @DisplayName("a subquery can produce columns the catalog never saw")
        void shouldNotClaimToKnowSubqueries() {
            final var scope = TableScope.of("select id from (select id from tenant) as inner_query");

            assertFalse(scope.exhaustive());
        }

        @Test
        @DisplayName("a common table expression is the same problem")
        void shouldNotClaimToKnowCommonTableExpressions() {
            final var scope = TableScope.of("with recent as (select id from tenant) select id from recent");

            assertFalse(scope.exhaustive());
        }

        @Test
        @DisplayName("a union reads from several selects at once")
        void shouldNotClaimToKnowSetOperations() {
            final var scope = TableScope.of("select id from tenant union select id from account");

            assertFalse(scope.exhaustive());
        }

        @Test
        @DisplayName("SQL it cannot parse names nothing")
        void shouldNotClaimToKnowUnparseableSql() {
            final var scope = TableScope.of("CREATE ALIAS getVersion AS 'String version() { return \"1\"; }'");

            assertAll(
                    () -> assertFalse(scope.exhaustive()),
                    () -> assertTrue(scope.tables().isEmpty()));
        }

        @Test
        void shouldNotClaimToKnowBlankStatements() {
            assertFalse(TableScope.of("   ").exhaustive());
        }

    }

}
