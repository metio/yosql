--
-- name: findItemByName
-- methodStandardApi: false
-- methodStreamEagerApi: false
-- methodStreamLazyApi: false
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
-- methodStandardApi: false
-- methodStreamEagerApi: false
-- methodStreamLazyApi: false
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
-- methodStandardApi: false
-- methodStreamEagerApi: false
-- methodStreamLazyApi: false
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
-- methodStandardApi: false
-- methodStreamEagerApi: false
-- methodStreamLazyApi: false
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
