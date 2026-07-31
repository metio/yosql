/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

UPDATE person
SET name = :name
WHERE id = :userId
;
