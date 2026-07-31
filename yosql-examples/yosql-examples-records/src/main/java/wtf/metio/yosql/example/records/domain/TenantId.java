/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.records.domain;

import java.util.UUID;

/**
 * A tenant's identity, so a method taking one cannot be handed any other UUID by mistake.
 *
 * <p>The factory is what tells the generator this is a value read from one column rather than a
 * record whose component reads a column called {@code value}.</p>
 */
public record TenantId(UUID value) {

    public static TenantId valueOf(final UUID value) {
        return new TenantId(value);
    }

}
