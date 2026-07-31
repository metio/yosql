/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
--
select *
from persons
where name = :name
;

--
-- name: findPerson
--
select *
from persons
where name = :name
;
