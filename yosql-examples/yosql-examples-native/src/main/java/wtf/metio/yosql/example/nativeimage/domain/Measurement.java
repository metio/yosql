/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.nativeimage.domain;

import java.math.BigDecimal;

/**
 * A number and the unit it was measured in — two columns, one value object.
 */
public record Measurement(BigDecimal amount, String unit) {
}
