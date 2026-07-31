/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

-- returning: SINGLE
-- parameters:
--   - name: pid
--     type: long
SELECT *
FROM companies
WHERE pid = :pid
;
