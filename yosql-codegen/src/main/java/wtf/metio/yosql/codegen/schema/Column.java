/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

/**
 * One column, as its DDL declared it.
 *
 * <p>{@code sqlType} is the type name exactly as written — {@code varchar}, {@code bigserial},
 * {@code timestamp with time zone} — because what it maps to in Java depends on the database, and
 * that decision belongs to whatever reads this rather than to the reader that wrote it.</p>
 *
 * <p>{@code nullable} is what the DDL says, not what the data happens to hold. A column is nullable
 * unless it is declared {@code not null} or is the table's primary key, which is the same rule every
 * database applies.</p>
 */
public record Column(String name, String sqlType, boolean nullable) {

    public Column {
        name = name.strip();
        // Two readers spell the same type differently — "numeric(12,2)" as written, "numeric (12,
        // 2)" once a parser has been through it. Canonicalise so a type compares equal either way.
        sqlType = sqlType.strip().toLowerCase(java.util.Locale.ROOT)
                .replaceAll("\\s+", " ")
                .replaceAll("\\s+\\(", "(")
                .replaceAll(",\\s+", ",")
                .replaceAll("\\s+\\)", ")");
    }

    /**
     * @return the type without its length, precision or scale — {@code varchar(64)} is a
     *         {@code varchar} as far as the Java type is concerned
     */
    public String baseType() {
        // Removed rather than cut at, because a standard type carries its precision in the middle:
        // `timestamp(6) with time zone` is a timestamptz, and cutting at the parenthesis would leave
        // a plain `timestamp` — a different Java type, and the spelling pg_dump writes.
        return sqlType.replaceAll("\\([^)]*\\)", " ")
                .replaceAll("\\s+", " ")
                .strip();
    }

}
