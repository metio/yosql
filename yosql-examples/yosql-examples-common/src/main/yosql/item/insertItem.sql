/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

--
-- parameters:
--   - name: id
--     type: int
--   - name: name
--     type: java.lang.String
--
INSERT INTO items (id, name)
VALUES (:id, :name)
;

-- createConnection: false
-- parameters:
--   - name: id
--     type: int
--   - name: name
--     type: java.lang.String
--
INSERT INTO items (id, name)
VALUES (:id, :name)
;
