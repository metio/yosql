/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.records.domain;

/**
 * Why a ledger entry exists.
 */
public enum Reason {
    TOP_UP,
    USAGE,
    REFUND,
    ADJUSTMENT
}
