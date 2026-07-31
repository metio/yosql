/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- parameters:
--   - name: department
--     type: long
DELETE
FROM departments
WHERE pid = :department
;
