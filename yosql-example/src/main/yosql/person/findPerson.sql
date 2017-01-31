--
-- parameters:
--   - name: name
--     type: java.lang.String
--
select  *
from    persons
where   name = :name
;
