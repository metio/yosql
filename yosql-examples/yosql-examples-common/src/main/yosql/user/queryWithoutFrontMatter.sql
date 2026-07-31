/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

select *
from users
where id = :id
  and name = :name
  and other = :id
;
