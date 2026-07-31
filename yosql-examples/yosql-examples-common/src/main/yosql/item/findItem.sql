/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

--
-- name: findItemByName
-- parameters:
--   - name: name
--     type: java.lang.String
-- resultRowConverter:
--   alias: itemConverter
--
select *
from items
where name = :name
;

--
-- name: findItemByName
-- vendor: H2
-- parameters:
--   - name: name
--     type: java.lang.String
-- resultRowConverter:
--   alias: itemConverter
--
select *
from items
where name = :name
;

--
-- name: findItemByAllNames
-- parameters:
--   - name: name
--     type: java.lang.String
-- resultRowConverter:
--   alias: itemConverter
--
select *
from items
where name = :name
   or other_name = :name
;

--
-- name: findItemBySnakeCaseName
-- parameters:
--   - name: name_of_item
--     type: java.lang.String
-- resultRowConverter:
--   alias: itemConverter
--
select *
from items
where name = :name_of_item
;
