/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
--
-- name: queryAdminUser
-- repository: Admin
--
select *
from users
where id = -1;
