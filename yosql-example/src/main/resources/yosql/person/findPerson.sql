--
-- name: findPerson
-- vendor: Microsoft SQL Server
-- #methodUknownVendorBehavior: *execute*|ignore|fail
-- parameters:
--   - name: name
--     type: java.lang.String
--
select  *
from    persons
where   name = :name
;

--
-- name: findPerson
-- vendor: H2
-- #methodUknownVendorBehavior: *execute*|ignore|fail|none
--
select  *
from    persons
where   name = :name
;

--
-- name: findPerson
-- #methodUknownVendorBehavior: *execute*|ignore|fail|none
--
select  *
from    persons
where   name = :name
;
