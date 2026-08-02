/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
-- parameters:
--   name: string
select *
from items
where name = :name;
