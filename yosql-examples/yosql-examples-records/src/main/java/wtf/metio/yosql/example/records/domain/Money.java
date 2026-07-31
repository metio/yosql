/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.example.records.domain;

import java.util.Currency;

/**
 * An amount in the minor unit of its currency — cents for EUR, and not always a hundredth
 * elsewhere, which is why the currency travels with the number rather than beside it.
 *
 * <p>The components read the columns {@code minor_units} and {@code currency}. The ledger stores
 * the amount as {@code amount_cents}, so the query aliases it — which puts the one place the two
 * naming schemes meet next to the column being renamed.</p>
 */
public record Money(long minorUnits, Currency currency) {
}
