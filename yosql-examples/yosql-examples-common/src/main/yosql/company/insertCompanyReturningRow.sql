/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 *
 * A write that answers with the one row it wrote, which is how a generated key
 * is usually read back.
 */
--
-- type: writing
-- returning: single
-- parameters:
--   - name: id
--     type: int
--   - name: name
--     type: java.lang.String
--
SELECT *
FROM FINAL TABLE (INSERT INTO companies (id, name) VALUES (:id, :name));
