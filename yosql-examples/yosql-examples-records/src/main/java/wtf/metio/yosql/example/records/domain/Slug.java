/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.records.domain;

import java.util.Locale;

/**
 * A URL-safe short name. The factory normalises, which is the other reason to have one: the
 * generated converter goes through it, so a value read from the database obeys the same rule as one
 * built in code.
 */
public record Slug(String value) {

    public static Slug valueOf(final String value) {
        return new Slug(value.strip().toLowerCase(Locale.ROOT));
    }

}
