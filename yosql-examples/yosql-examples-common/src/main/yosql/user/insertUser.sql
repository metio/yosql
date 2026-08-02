/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
-- parameters:
--   id: int
--   name: string
INSERT INTO users (id, name)
VALUES (:id, :name);
