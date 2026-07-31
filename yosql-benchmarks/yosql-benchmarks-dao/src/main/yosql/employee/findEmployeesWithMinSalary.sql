/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- parameters:
--   - name: salary
--     type: long
SELECT *
FROM employees
WHERE salary >= :salary
;
