/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 *
 * A write that answers with the rows it wrote. FINAL TABLE is how H2 spells it;
 * PostgreSQL writes INSERT ... RETURNING *.
 */
--
-- type: writing
-- returning: multiple
-- parameters:
--   - name: id
--     type: int
--   - name: name
--     type: java.lang.String
--
SELECT *
FROM FINAL TABLE (INSERT INTO companies (id, name) VALUES (:id, :name));
