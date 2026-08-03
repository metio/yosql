/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The schema of every database a project targets, not just of one.
 *
 * <p>A project that supports more than one database writes its DDL more than once — {@code bigserial}
 * on PostgreSQL, {@code bigint auto_increment} on MySQL — and marks each with the same
 * {@code vendor} its statements use. Reading all of that into one catalog would let whichever file
 * was read last decide what a column is, which would make a generated method signature depend on
 * directory order.</p>
 *
 * <p>So there is a catalog per vendor, plus a shared one for DDL that names no vendor. A vendor's
 * view is the shared catalog with its own tables layered over it, which is what lets a project put
 * the tables that are the same in one place and only repeat the ones that differ.</p>
 */
public final class Schemas {

    private final Catalog shared;
    private final Map<String, Catalog> byVendor;

    private Schemas(final Catalog shared, final Map<String, Catalog> byVendor) {
        this.shared = shared;
        this.byVendor = byVendor;
    }

    public static Schemas empty() {
        return new Schemas(Catalog.empty(), Map.of());
    }

    /**
     * @param statements every statement that might be DDL, each with the vendor it was written for,
     *                   in the order it applies
     */
    public static Schemas of(final List<VendorStatement> statements) {
        final var sharedSql = new ArrayList<String>();
        final var vendorSql = new LinkedHashMap<String, List<String>>();
        for (final var statement : statements) {
            statement.vendor()
                    .map(Catalog::normalize)
                    .ifPresentOrElse(
                            vendor -> vendorSql.computeIfAbsent(vendor, _ -> new ArrayList<>()).add(statement.sql()),
                            () -> sharedSql.add(statement.sql()));
        }
        final var shared = CatalogReader.read(sharedSql);
        final var byVendor = new LinkedHashMap<String, Catalog>(vendorSql.size());
        vendorSql.forEach((vendor, sql) -> {
            // The shared DDL applies to every database, so a vendor sees it too and then adds its
            // own on top.
            final var combined = new ArrayList<>(sharedSql);
            combined.addAll(sql);
            byVendor.put(vendor, CatalogReader.read(combined));
        });
        return new Schemas(shared, Map.copyOf(byVendor));
    }

    /**
     * @return what a statement written for that vendor sees; the shared catalog when the vendor has
     *         no DDL of its own, because the tables it needs may be the ones nobody had to repeat
     */
    public Catalog forVendor(final Optional<String> vendor) {
        return vendor.map(Catalog::normalize)
                .map(byVendor::get)
                .orElse(shared);
    }

    /**
     * Every catalog a statement without a vendor could run against.
     *
     * <p>Such a statement is the fallback for any database not named, so it has to hold everywhere:
     * a column must exist in all of them, and a type inferred from one has to agree with the rest.
     * When no vendor-specific DDL exists at all, that is just the shared catalog.</p>
     */
    public List<Catalog> everywhere() {
        if (byVendor.isEmpty()) {
            return List.of(shared);
        }
        return List.copyOf(byVendor.values());
    }

    /**
     * @return the catalogs a statement has to be valid against
     */
    public List<Catalog> applicableTo(final Optional<String> vendor) {
        return vendor.isPresent() ? List.of(forVendor(vendor)) : everywhere();
    }

    public boolean isEmpty() {
        return shared.isEmpty() && byVendor.values().stream().allMatch(Catalog::isEmpty);
    }

    /**
     * One statement and the database it was written for.
     */
    public record VendorStatement(Optional<String> vendor, String sql) {
    }

}
