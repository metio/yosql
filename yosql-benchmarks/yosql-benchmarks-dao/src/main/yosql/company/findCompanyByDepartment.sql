/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
