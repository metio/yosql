/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.nativeimage.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * One sensor reading.
 *
 * <p>Everything the mapping has to get right in a native image is here: a UUID, an enum read from
 * text, a value object built from two columns, a timestamp that is set and one that is not.</p>
 */
public record Reading(
        UUID id,
        String sensorId,
        Level level,
        Measurement measurement,
        Instant recordedAt,
        Instant clearedAt) {
}
