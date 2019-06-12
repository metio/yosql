--
-- parameters:
--   - name: min
--     type: int
--   - name: max
--     type: int
--
select  *
from    companies
where   id < :max
  and   id > :min
;
