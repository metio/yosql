/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.records;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SelectedColumns")
class SelectedColumnsTest {

    @Nested
    @DisplayName("names it can read")
    class Readable {

        @Test
        @DisplayName("reads a plain column list")
        void plainColumns() {
            assertEquals(List.of("id", "slug", "name"),
                    SelectedColumns.of("select id, slug, name from tenant").orElseThrow());
        }

        @Test
        @DisplayName("reads a column list spread over several lines")
        void multiline() {
            assertEquals(List.of("id", "account_id", "created_at"), SelectedColumns.of("""
                    select id,
                           account_id,
                           created_at
                    from tenant
                    where id = ?
                    """).orElseThrow());
        }

        @Test
        @DisplayName("takes the alias when a column has one")
        void alias() {
            assertEquals(List.of("id", "minor_units", "at"),
                    SelectedColumns.of("select id, amount_cents as minor_units, created_at as at from ledger")
                            .orElseThrow());
        }

        @Test
        @DisplayName("takes a quoted alias verbatim")
        void quotedAlias() {
            assertEquals(List.of("minorUnits"),
                    SelectedColumns.of("select amount_cents as \"minorUnits\" from ledger").orElseThrow());
        }

        @Test
        @DisplayName("drops a table qualifier")
        void qualified() {
            assertEquals(List.of("id", "slug"),
                    SelectedColumns.of("select t.id, t.slug from tenant t").orElseThrow());
        }

        @Test
        @DisplayName("takes the alias of an expression")
        void aliasedExpression() {
            assertEquals(List.of("currency", "minor_units"),
                    SelectedColumns.of("select currency, sum(amount_cents) as minor_units from ledger group by currency")
                            .orElseThrow());
        }

        @Test
        @DisplayName("is not confused by commas inside a function call")
        void commasInsideCall() {
            assertEquals(List.of("id", "label"),
                    SelectedColumns.of("select id, coalesce(name, slug, 'unknown') as label from tenant")
                            .orElseThrow());
        }

        @Test
        @DisplayName("is not confused by 'from' inside a function call")
        void fromInsideCall() {
            assertEquals(List.of("year"),
                    SelectedColumns.of("select extract(year from created_at) as year from tenant").orElseThrow());
        }

        @Test
        @DisplayName("is not confused by 'from' inside a string literal")
        void fromInsideLiteral() {
            assertEquals(List.of("id", "origin"),
                    SelectedColumns.of("select id, 'imported from legacy' as origin from tenant").orElseThrow());
        }

        @Test
        @DisplayName("reads a distinct select")
        void distinct() {
            assertEquals(List.of("currency"),
                    SelectedColumns.of("select distinct currency from ledger").orElseThrow());
        }

        @Test
        @DisplayName("does not care about keyword case")
        void keywordCase() {
            assertEquals(List.of("id"), SelectedColumns.of("SELECT ID FROM TENANT").orElseThrow());
        }

        @Test
        @DisplayName("lower-cases an unquoted name, which is what the driver reports")
        void unquotedNamesAreLowerCased() {
            assertEquals(List.of("tenant_id"), SelectedColumns.of("select Tenant_Id from tenant").orElseThrow());
        }

        @Test
        @DisplayName("reads the outer select's list, not a subquery's")
        void subqueryInWhere() {
            assertEquals(List.of("id", "slug"), SelectedColumns.of("""
                    select id, slug
                    from tenant
                    where account_id in (select id from account)
                    """).orElseThrow());
        }

    }

    @Nested
    @DisplayName("names it cannot read")
    class Unreadable {

        @Test
        @DisplayName("gives up on a star select")
        void star() {
            assertTrue(SelectedColumns.of("select * from tenant").isEmpty());
        }

        @Test
        @DisplayName("gives up on a qualified star")
        void qualifiedStar() {
            assertTrue(SelectedColumns.of("select t.* from tenant t").isEmpty());
        }

        @Test
        @DisplayName("gives up on an unaliased expression")
        void unaliasedExpression() {
            assertTrue(SelectedColumns.of("select sum(amount_cents) from ledger").isEmpty());
        }

        @Test
        @DisplayName("gives up when one item of many is unreadable")
        void oneUnreadableItem() {
            assertTrue(SelectedColumns.of("select id, sum(amount_cents) from ledger group by id").isEmpty());
        }

        @Test
        @DisplayName("gives up on a statement that is not a select")
        void notASelect() {
            assertTrue(SelectedColumns.of("insert into tenant (id) values (?)").isEmpty());
            assertTrue(SelectedColumns.of("update tenant set slug = ? where id = ?").isEmpty());
            assertTrue(SelectedColumns.of("delete from tenant where id = ?").isEmpty());
        }

        @Test
        @DisplayName("gives up on a select without a from clause")
        void noFrom() {
            assertTrue(SelectedColumns.of("select 1").isEmpty());
        }

        @Test
        @DisplayName("gives up on a subquery in the select list")
        void subqueryInSelectList() {
            assertTrue(SelectedColumns.of("select id, (select count(*) from ledger) from tenant").isEmpty());
        }

    }

}
