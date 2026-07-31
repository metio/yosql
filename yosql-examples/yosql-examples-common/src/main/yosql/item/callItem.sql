/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

select *
from items
where name = :name
;

-- createConnection: false
select *
from items
where name = :name
;
