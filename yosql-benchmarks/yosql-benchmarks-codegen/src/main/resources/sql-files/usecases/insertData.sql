/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- parameters:
--   userId: int
--   name: string
INSERT INTO users (id, name)
VALUES (:userId, :name)
;
