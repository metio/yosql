/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

-- parameters:
--   - name: cost
--     type: long
SELECT  p.*
FROM    projects p
    INNER JOIN (
        SELECT  pe.project_pid, sum(e.salary) as cost
        FROM    projectEmployees pe
            INNER JOIN employees e
               ON pe.employee_pid = e.pid
        GROUP BY pe.project_pid
    ) data ON p.pid = data.project_pid
WHERE   data.cost >= :cost
;
