/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- parameters:
--   - name: name
--     type: java.lang.String
--   - name: address
--     type: java.lang.String
INSERT INTO companies (name, address)
VALUES (:name, :address)
;
