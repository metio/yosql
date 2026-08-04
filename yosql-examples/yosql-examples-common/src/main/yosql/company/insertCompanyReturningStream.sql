/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 *
 * The same write, read back one row at a time. The stream owns the connection,
 * the statement and the result set until it is closed.
 */
--
-- type: writing
-- returning: cursor
-- parameters:
--   - name: id
--     type: int
--   - name: name
--     type: java.lang.String
--
SELECT *
FROM FINAL TABLE (INSERT INTO companies (id, name) VALUES (:id, :name));
