/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.nativeimage.domain;

import java.util.UUID;

/**
 * What `insert … returning id` hands back: a row of one column, named after the column it reads.
 */
public record ReadingId(UUID id) {
}
