/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The tables the build knows about, read from the project's own DDL.
 *
 * <p>Nothing here comes from a database. Generation has to work in a checkout with no services
 * running, which is why the catalog is read from `create table` statements the project already
 * keeps rather than from a connection.</p>
 *
 * <p>A catalog is deliberately partial. A table whose DDL the reader could not understand is absent
 * rather than empty, and the difference matters: absent means "nothing is known about this table",
 * which skips every check that would have used it. Empty would mean "this table has no columns",
 * which would fail all of them.</p>
 */
public final class Catalog {

    private final Map<String, Table> tables;
    private final Optional<String> vendor;

    private Catalog(final Map<String, Table> tables, final Optional<String> vendor) {
        this.tables = tables;
        this.vendor = vendor;
    }

    public static Catalog of(final Map<String, Table> tables) {
        final var byLowerCaseName = new LinkedHashMap<String, Table>(tables.size());
        tables.forEach((name, table) -> byLowerCaseName.put(normalize(name), table));
        return new Catalog(Collections.unmodifiableMap(byLowerCaseName), Optional.empty());
    }

    public static Catalog empty() {
        return new Catalog(Map.of(), Optional.empty());
    }

    /**
     * @return the same tables, marked as having been read from the DDL of that database
     */
    public Catalog describing(final String databaseVendor) {
        return new Catalog(tables, Optional.of(databaseVendor));
    }

    /**
     * Whose spellings this catalog's column types are written in.
     *
     * <p>A catalog read from DDL marked with a {@code vendor} holds that database's columns whatever
     * the statement reading them says: {@code bytea} is PostgreSQL's spelling because the DDL was
     * PostgreSQL's, not because a query asked for it. Reading such a column with the statement's
     * vendor instead left every vendor-specific spelling unknown for the statements that need no
     * vendor of their own — which is most of them, since {@code vendor} exists to tell databases
     * apart and a project with one database has none to tell apart.</p>
     *
     * <p>The shared catalog was written for no database in particular, so there the statement's own
     * vendor is the best answer available, and a project that marks its statements but not its DDL
     * keeps reading its columns exactly as it did.</p>
     *
     * @param statementVendor what the statement declares, used when the DDL declared nothing
     */
    public Optional<String> dialect(final Optional<String> statementVendor) {
        return vendor.or(() -> statementVendor);
    }

    /**
     * SQL identifiers are case-insensitive unless quoted, and projects disagree about which case to
     * write them in. Everything is compared lower-cased so that {@code TENANT}, {@code Tenant} and
     * {@code tenant} are one table.
     */
    public static String normalize(final String identifier) {
        return unquote(identifier).toLowerCase(Locale.ROOT);
    }

    /**
     * Drops the quotes a dialect wraps an identifier in.
     *
     * <p>Every side of a comparison has to agree about this or none of them meet: a parser hands back
     * {@code "firstName"} with its quotes for quoted DDL, and the query reading it may spell the same
     * column bare. Unquoting in one place keeps the catalog's keys and the names it reports in terms
     * of the identifier itself, which is also the only spelling a generated record component can be
     * named after.</p>
     *
     * <p>Covers the two pairs the parser understands — {@code "..."} in standard SQL and
     * {@code `...`} in MySQL — and undoes the doubling that escapes the quote inside one. SQL
     * Server's {@code [...]} is absent because DDL written that way does not parse as a
     * {@code create table} at all, so no such identifier ever reaches a catalog.</p>
     */
    public static String unquote(final String identifier) {
        final var stripped = identifier.strip();
        if (stripped.length() < 2) {
            return stripped;
        }
        final var first = stripped.charAt(0);
        final var last = stripped.charAt(stripped.length() - 1);
        final var inner = stripped.substring(1, stripped.length() - 1);
        if (first == '"' && last == '"') {
            return inner.replace("\"\"", "\"");
        }
        if (first == '`' && last == '`') {
            return inner.replace("``", "`");
        }
        return stripped;
    }

    /**
     * @return the table, or empty when nothing is known about a table of that name
     */
    public Optional<Table> table(final String name) {
        return Optional.ofNullable(tables.get(normalize(name)));
    }

    public Set<String> tableNames() {
        return tables.keySet();
    }

    /**
     * The table a column named by a statement is declared in.
     *
     * <p>A qualifier settles it: {@code c.id} is the {@code customers} row's id whatever
     * {@code orders} happens to declare. Guessing instead — taking the first table in scope with a
     * column of that name — reads the wrong type for every self-join and every pair of tables
     * sharing a column name, which is most schemas.</p>
     *
     * <p>Without a qualifier the statement itself did not say, so the first table declaring the name
     * is the only answer available; SQL would have refused an ambiguous one.</p>
     *
     * @param column the column as the statement writes it, qualified or not
     * @param scope what the statement reads from
     */
    public Optional<Table> declaringTable(final String column, final TableScope scope) {
        final var dot = column.lastIndexOf('.');
        final var name = dot < 0 ? column : column.substring(dot + 1);
        if (dot >= 0) {
            return scope.resolve(column.substring(0, dot))
                    .flatMap(this::table)
                    .filter(table -> table.column(name).isPresent());
        }
        return scope.tables().stream()
                .map(this::table)
                .flatMap(Optional::stream)
                .filter(table -> table.column(name).isPresent())
                .findFirst();
    }

    public boolean isEmpty() {
        return tables.isEmpty();
    }

}
