/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

--
-- name: updateUser
-- repository: AdminRepository
-- parameters:
--   name: string
--   id: int
--
update users
set name = :name
where id = :id
;

-- createConnection: false
-- repository: AdminRepository
-- parameters:
--   name: string
--   id: int
--
update users
set name = :name
where id = :id
;
