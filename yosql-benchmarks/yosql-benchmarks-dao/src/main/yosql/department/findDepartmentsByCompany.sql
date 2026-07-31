/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

-- parameters:
--   - name: company
--     type: long
SELECT *
FROM departments
WHERE company_pid = :company
;
