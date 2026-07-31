/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- returning: SINGLE
-- parameters:
--   - name: pid
--     type: long
SELECT *
FROM companies
WHERE pid = :pid
;
