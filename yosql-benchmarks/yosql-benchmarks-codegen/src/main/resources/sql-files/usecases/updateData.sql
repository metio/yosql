/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- parameters:
--   name: string
--   userId: int
UPDATE person
SET name = :name
WHERE id = :userId
;
