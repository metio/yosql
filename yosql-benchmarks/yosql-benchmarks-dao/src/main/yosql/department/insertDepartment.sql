/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

-- parameters:
--   - name: company
--     type: java.lang.Long
--   - name: name
--     type: java.lang.String
INSERT INTO departments (company_pid, name)
VALUES (:company, :name)
;
