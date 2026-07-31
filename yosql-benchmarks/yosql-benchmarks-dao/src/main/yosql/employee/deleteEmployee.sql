/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- parameters:
--   - name: employee
--     type: long
DELETE
FROM employees
WHERE pid = :employee
;
