/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

-- parameters:
--   - name: company
--     type: java.lang.Long
--   - name: oldCompany
--     type: java.lang.Long
--   - name: name
--     type: java.lang.String
UPDATE departments
SET company_pid = :company,
    name        = :name
WHERE company_pid = :oldCompany
;
