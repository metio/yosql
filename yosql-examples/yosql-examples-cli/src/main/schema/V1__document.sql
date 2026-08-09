/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- Kept apart from the statements, so that `schema.sqlStatementsDirectory` is a directory the
-- generator only reads because the build said to. Named the way a migration tool names its files,
-- since that is where most projects keep this.

create table document (
    id      uuid  not null primary key,
    -- Deliberately a type only PostgreSQL spells. It resolves when `schema.vendor` reaches the
    -- generator and fails the build when it does not, which is what makes the record below evidence
    -- rather than decoration.
    payload jsonb not null,
    revision int   not null
)
;
