/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.records.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * An order placed by a tenant.
 *
 * <p>{@code activatedAt} and {@code cancelledAt} are legitimately null: an order that is still a
 * draft has never been activated, and one that ran its term was never cancelled. They arrive as
 * {@code null}, not as the epoch.</p>
 */
public record PlacedOrder(
        UUID id,
        UUID tenantId,
        OrderState state,
        BigDecimal monthlyPrice,
        Instant createdAt,
        Instant activatedAt,
        Instant cancelledAt) {
}
