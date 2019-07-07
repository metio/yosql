--
-- name: updateUser
-- repository: com.example.persistence.AdminRepository
--
update users
set id = 1
where id = :id
  and name = :name
;
