/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- parameters:
--   - name: name
--     type: java.lang.String
--   - name: date
--     type: java.lang.Long
INSERT INTO projects (NAME, DATESTARTED)
VALUES (:name, :date)
;
