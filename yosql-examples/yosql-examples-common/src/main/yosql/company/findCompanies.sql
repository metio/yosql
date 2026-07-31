/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

--
-- parameters:
--   - name: min
--     type: int
--   - name: max
--     type: int
-- annotations:
--   - type: wtf.metio.yosql.example.common.CustomAnnotation
--
select *
from companies
where id < :max
  and id > :min
;

-- createConnection: false
-- parameters:
--   - name: min
--     type: int
--   - name: max
--     type: int
-- annotations:
--   - type: wtf.metio.yosql.example.common.CustomAnnotation
--
select *
from companies
where id < :max
  and id > :min
;
