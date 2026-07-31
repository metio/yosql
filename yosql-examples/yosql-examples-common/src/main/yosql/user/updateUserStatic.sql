/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

--
-- repository: AdminRepository
--
update users
set name = 'static value'
where id = 1
;

-- createConnection: false
-- repository: AdminRepository
update users
set name = 'static value'
where id = 1
;
