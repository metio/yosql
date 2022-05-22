/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

-- parameters:
--   - name: department
--     type: long
SELECT  c.*
FROM    companies c
    INNER JOIN departments d
        ON c.pid = d.company_pid
        AND d.pid = :department
;
