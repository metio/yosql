/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
--
-- parameters:
--   - name: id
--     type: int
--   - name: name
--     type: java.lang.String
--
INSERT INTO items (id, name)
VALUES (:id, :name);
