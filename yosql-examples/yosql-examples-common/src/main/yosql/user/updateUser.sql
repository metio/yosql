/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

--
-- name: updateUser
-- repository: AdminRepository
--
update users
set name = :name
where id = :id
;

-- createConnection: false
-- repository: AdminRepository
--
update users
set name = :name
where id = :id
;
