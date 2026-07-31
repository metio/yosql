/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

UPDATE person
SET name = :name
WHERE id = :userId
;
