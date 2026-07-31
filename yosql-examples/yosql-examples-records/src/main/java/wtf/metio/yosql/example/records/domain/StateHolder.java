/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.records.domain;

import java.util.UUID;

/**
 * A record reading an enum out of a text column, for the case where the text is not one of the
 * constants.
 */
public record StateHolder(UUID id, OrderState state) {
}
