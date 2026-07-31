/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

--
-- name: querySpecialUserWithConstantId
-- returning: single
--
select *
from users
where id = -1
;

-- createConnection: false
-- returning: single
--
select *
from users
where id = -1
;
