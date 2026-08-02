/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
  and id > :min;
