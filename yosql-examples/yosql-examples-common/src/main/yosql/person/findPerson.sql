/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

--
-- name: findPerson
-- vendor: Microsoft SQL Server
-- parameters:
--   - name: name
--     type: java.lang.String
--
select *
from persons
where name = :name
;

--
-- name: findPerson
-- vendor: H2
-- parameters:
--   name: string
--
select *
from persons
where name = :name
;

--
-- name: findPerson
-- parameters:
--   name: string
--
select *
from persons
where name = :name
;
