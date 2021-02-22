/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

--
-- name: findItemByName
-- parameters:
--   - name: name
--     type: java.lang.String
-- resultRowConverter:
--   alias: itemConverter
--
select  *
from    items
where   name = :name
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
select  *
from    items
where   other_name = :name
;

--
-- name: findItemByAllNames
-- parameters:
--   - name: name
--     type: java.lang.String
-- resultRowConverter:
--   alias: itemConverter
--
select  *
from    items
where   name = :name
  or    other_name = :name
  or    last_name = :name
;

--
-- name: findItemBySnakeCaseName
-- parameters:
--   - name: name_of_item
--     type: java.lang.String
-- resultRowConverter:
--   alias: itemConverter
--
select  *
from    items
where   name = :name_of_item
;
