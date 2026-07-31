/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

-- parameters:
--   - name: department
--     type: long
SELECT c.*
FROM companies c
         INNER JOIN departments d
                    ON c.pid = d.company_pid
                        AND d.pid = :department
;
