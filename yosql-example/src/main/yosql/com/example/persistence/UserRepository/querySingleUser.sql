--
-- parameters:
--   - name: id
--     type: "java.lang.Integer"
--
select  *
from    users
where   id = :id
;
