--
-- name: queryAdminUser
-- repository: com.example.persistence.AdminRepository
--
select  *
from    users
where   id = -1
;
