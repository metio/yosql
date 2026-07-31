/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
where name = :name
;

-- createConnection: false
-- parameters:
--   - name: name
--     type: java.lang.String
-- returning: single
-- throwOnMultipleResults: true
--
select *
from companies
where name = :name
;
