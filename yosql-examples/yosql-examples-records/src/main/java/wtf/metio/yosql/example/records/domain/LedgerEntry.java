/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.example.records.domain;

import java.time.Instant;

/**
 * One movement on a tenant's balance.
 *
 * <p>{@code amount} is a value object built from two columns of the same row. Nesting groups them
 * on this side; the query still selects a flat list, so each component of {@link Money} claims the
 * column matching its own name.</p>
 */
public record LedgerEntry(long id, Money amount, Reason reason, String reference, Instant at) {
}
