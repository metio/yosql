--
-- name: findItemByName
-- generateStandardApi: false
-- generateStreamEagerApi: false
-- generateStreamLazyApi: false
-- parameters:
--   - name: name
--     type: java.lang.String
-- resultConverter:
--   alias: itemConverter
--
select  *
from    items
where   name = :name
;

--
-- name: findItemByName
-- vendor: H2
-- generateStandardApi: false
-- generateStreamEagerApi: false
-- generateStreamLazyApi: false
-- parameters:
--   - name: name
--     type: java.lang.String
-- resultConverter:
--   alias: itemConverter
--
select  *
from    items
where   other_name = :name
;

--
-- name: findItemByAllNames
-- generateStandardApi: false
-- generateStreamEagerApi: false
-- generateStreamLazyApi: false
-- parameters:
--   - name: name
--     type: java.lang.String
-- resultConverter:
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
-- generateStandardApi: false
-- generateStreamEagerApi: false
-- generateStreamLazyApi: false
-- parameters:
--   - name: name_of_item
--     type: java.lang.String
-- resultConverter:
--   alias: itemConverter
--
select  *
from    items
where   name = :name_of_item
;
