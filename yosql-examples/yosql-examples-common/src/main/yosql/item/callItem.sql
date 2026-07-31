/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
