/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
--
-- name: querySpecialUserWithConstantId
-- returning: single
--
select *
from users
where id = -1;
