/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.records.domain;

import java.util.UUID;

/**
 * A record whose {@code countValue} is a primitive while the column it reads is nullable.
 *
 * <p>The pairing is deliberate: {@code getLong} answers {@code 0} for SQL NULL, so a mapper that
 * did not look would hand back a plausible zero. The generated one refuses.</p>
 */
public record Sample(UUID id, long countValue) {
}
