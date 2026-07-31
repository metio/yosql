/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
