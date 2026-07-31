/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- parameters:
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
INSERT INTO employees (department_pid, name, surname, email, salary)
VALUES (:department, :name, :surname, :email, :salary)
;
