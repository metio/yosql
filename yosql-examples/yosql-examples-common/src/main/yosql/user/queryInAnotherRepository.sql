/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

--
-- name: queryAdminUser
-- repository: Admin
--
select *
from users
where id = -1
;

-- createConnection: false
-- repository: Admin
--
select *
from users
where id = -1
;
