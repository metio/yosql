--
--
select  *
from    persons
where   name = :name
;

--
-- name: findPerson
-- vendor: Microsoft SQL Server
-- parameters:
--   - name: name
--     type: java.lang.String
--
select  *
from    persons
where   name = :name
;
