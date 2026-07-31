/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- parameters:
--   - name: company
--     type: long
SELECT *
FROM departments
WHERE company_pid = :company
;
