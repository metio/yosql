/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

--
-- parameters:
--   - name: userId
--     type: int
--   - name: name
--     type: java.lang.String
--
select *
from users
where id = :userId
  and name = :name
;

-- createConnection: false
-- parameters:
--   - name: userId
--     type: int
--   - name: name
--     type: java.lang.String
--
select *
from users
where id = :userId
  and name = :name
;
