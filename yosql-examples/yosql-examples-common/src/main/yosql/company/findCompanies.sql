/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
