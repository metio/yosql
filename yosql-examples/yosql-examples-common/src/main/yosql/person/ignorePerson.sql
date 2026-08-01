/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- type: writing
-- executeOnce: false
-- executeBatch: false
INSERT INTO persons (id, name)
VALUES (:id, :name)
;
