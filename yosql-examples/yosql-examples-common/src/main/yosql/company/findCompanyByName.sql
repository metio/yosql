/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
--
-- parameters:
--   - name: name
--     type: java.lang.String
-- returning: single
-- throwOnMultipleResults: true
--
select *
from companies
where name = :name;
