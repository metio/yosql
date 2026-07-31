/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.records.domain;

/**
 * A balance in cents, wrapped around a primitive — so a NULL in that column is still refused before
 * the factory ever sees it.
 */
public record Cents(long value) {

    public static Cents valueOf(final long value) {
        return new Cents(value);
    }

}
