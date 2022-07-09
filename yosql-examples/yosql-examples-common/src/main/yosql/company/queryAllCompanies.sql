/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

--
-- annotations:
--   - type: wtf.metio.yosql.example.common.CustomAnnotationWithData
--     members:
--       - key: someChar
--         value: a
--         type: char
--       - key: someBool
--         value: true
--         type: boolean
--       - key: someByte
--         value: 123
--         type: byte
--       - key: someShort
--         value: 123
--         type: short
--       - key: someInt
--         value: 123
--         type: int
--       - key: someLong
--         value: 123
--         type: long
--       - key: someFloat
--         value: 3.14f
--         type: float
--       - key: someDouble
--         value: 3.14
--         type: double
--
select *
from companies
;

-- createConnection: false
-- annotations:
--   - type: wtf.metio.yosql.example.common.CustomAnnotationWithData
--     members:
--       - key: someChar
--         value: a
--         type: char
--       - key: someBool
--         value: true
--         type: boolean
--       - key: someByte
--         value: 123
--         type: byte
--       - key: someShort
--         value: 123
--         type: short
--       - key: someInt
--         value: 123
--         type: int
--       - key: someLong
--         value: 123
--         type: long
--       - key: someFloat
--         value: 3.14f
--         type: float
--       - key: someDouble
--         value: 3.14
--         type: double
--
select *
from companies
;
