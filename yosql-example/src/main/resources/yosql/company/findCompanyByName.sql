--
-- parameters:
--   - name: name
--     type: java.lang.String
--
select  *
from    companies
where   name = :name
;
