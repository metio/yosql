/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

-- parameters:
--   - name: pid
--     type: long
--   - name: department
--     type: long
--   - name: name
--     type: java.lang.String
--   - name: surname
--     type: java.lang.String
--   - name: email
--     type: java.lang.String
--   - name: salary
--     type: long
UPDATE employees
SET department_pid = :department,
    name           = :name,
    surname        = :surname,
    email          = :email,
    salary         = :salary
WHERE pid = :pid
;
