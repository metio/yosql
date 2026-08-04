/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 *
 * A read whose rows nobody wants: the statement is run for what it does to the
 * database rather than for what it answers, which is what FOR UPDATE is for.
 */
--
-- type: reading
-- returning: none
--
SELECT id
FROM companies
WHERE name = :name
    FOR UPDATE;
