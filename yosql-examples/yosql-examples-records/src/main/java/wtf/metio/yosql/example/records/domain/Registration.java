/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.records.domain;

import java.time.Instant;

/**
 * A row built entirely out of types that know how to build themselves from a column.
 */
public record Registration(TenantId tenantId, Slug slug, Cents balance, Instant registeredAt) {
}
