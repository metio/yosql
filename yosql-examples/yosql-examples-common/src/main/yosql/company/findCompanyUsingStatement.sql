/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

--
-- usePreparedStatement: true
-- parameters:
--   - name: name
--     type: java.lang.String
--
select *
from companies
where name = :name
;

-- createConnection: false
-- usePreparedStatement: true
-- parameters:
--   - name: name
--     type: java.lang.String
--
select *
from companies
where name = :name
;
