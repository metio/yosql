/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

-- parameters:
--   - name: salary
--     type: long
SELECT *
FROM employees
WHERE salary >= :salary
;
