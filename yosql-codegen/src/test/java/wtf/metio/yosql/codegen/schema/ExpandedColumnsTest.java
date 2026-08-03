/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ExpandedColumns")
class ExpandedColumnsTest {

    private static final Catalog CATALOG = CatalogReader.read(List.of(
            "create table tenant (id uuid not null primary key, slug varchar(64), created_at timestamp)",
            "create table account (id uuid not null primary key, name varchar(64))"));

    private static List<String> of(final String sql) {
        return ExpandedColumns.of(sql, TableScope.of(sql), CATALOG).orElse(List.of());
    }

    @Test
    @DisplayName("a star names what the table declares, in declaration order")
    void shouldExpandAStar() {
        assertIterableEquals(List.of("id", "slug", "created_at"), of("select * from tenant"));
    }

    @Test
    @DisplayName("a qualified star names only that table")
    void shouldExpandAQualifiedStar() {
        assertIterableEquals(List.of("account", "id", "name").subList(1, 3),
                of("select a.* from tenant t join account a on a.id = t.id"));
    }

    @Test
    @DisplayName("an unqualified star across a join names every table in order")
    void shouldExpandAcrossAJoin() {
        assertIterableEquals(List.of("id", "slug", "created_at", "id", "name"),
                of("select * from tenant join account on account.id = tenant.id"));
    }

    @Test
    @DisplayName("a written-out select list is left exactly as it was")
    void shouldLeaveWrittenListsAlone() {
        assertIterableEquals(List.of("id", "slug"), of("select id, slug from tenant"));
    }

    @Test
    @DisplayName("a table the catalog never saw could contribute any column")
    void shouldNotExpandUnknownTables() {
        assertTrue(of("select * from some_view").isEmpty());
    }

    @Test
    @DisplayName("a star mixed with anything else is not a star this expands")
    void shouldNotExpandMixedSelectLists() {
        assertAll(
                () -> assertTrue(of("select *, 1 as extra from tenant").isEmpty()),
                () -> assertTrue(of("select count(*) from tenant").isEmpty()));
    }

    @Test
    @DisplayName("a subquery is still unknown, star or not")
    void shouldNotExpandSubqueries() {
        assertTrue(of("select * from (select id from tenant) x").isEmpty());
    }

}
