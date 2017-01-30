--
-- parameters:
--   - name: userId
--     type: int
--   - name: name
--     type: java.lang.String
--
select  *
from    users
where   id = :userId
  and   name = :name
;
