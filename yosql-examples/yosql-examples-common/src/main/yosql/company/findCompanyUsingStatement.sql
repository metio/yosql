/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
--
-- usePreparedStatement: true
-- parameters:
--   - name: name
--     type: java.lang.String
--
select *
from companies
where name = :name;
