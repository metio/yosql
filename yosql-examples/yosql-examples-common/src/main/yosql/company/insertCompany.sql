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
INSERT INTO companies (id, name)
VALUES (:id, :name)
;

-- createConnection: false
-- parameters:
--   - name: id
--     type: int
--   - name: name
--     type: java.lang.String
--
INSERT INTO companies (id, name)
VALUES (:id, :name)
;
