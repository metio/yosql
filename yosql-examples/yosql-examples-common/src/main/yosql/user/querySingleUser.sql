/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
  and name = :name;
