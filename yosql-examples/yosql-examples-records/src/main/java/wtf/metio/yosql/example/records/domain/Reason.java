/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
