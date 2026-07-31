/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.records.domain;

import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

/**
 * A billing tenant. Component names read as snake_case give the columns: {@code accountId} reads
 * {@code account_id}, {@code createdAt} reads {@code created_at}.
 */
public record Tenant(
        UUID id,
        UUID accountId,
        String slug,
        String name,
        Currency currency,
        String timeZone,
        String language,
        Instant createdAt) {
}
