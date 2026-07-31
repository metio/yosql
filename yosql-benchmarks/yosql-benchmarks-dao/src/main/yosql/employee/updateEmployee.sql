/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
