/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

-- parameters:
--   - name: name
--     type: java.lang.String
--   - name: date
--     type: java.lang.Long
INSERT INTO projects (NAME, DATESTARTED)
VALUES (:name, :date)
;
