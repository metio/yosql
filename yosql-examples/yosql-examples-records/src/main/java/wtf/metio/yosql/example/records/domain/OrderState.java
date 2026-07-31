/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.example.records.domain;

/**
 * Where an order sits in its lifecycle.
 */
public enum OrderState {
    DRAFT,
    ACTIVE,
    CANCELLED,
    EXPIRED
}
