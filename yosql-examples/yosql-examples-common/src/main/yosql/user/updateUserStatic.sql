/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
