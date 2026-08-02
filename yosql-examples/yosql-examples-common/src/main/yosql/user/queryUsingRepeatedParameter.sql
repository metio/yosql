/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- parameters:
--   id: int
--   name: string
select *
from users
where id = :id
  and name = :name
  and other = :id
;
