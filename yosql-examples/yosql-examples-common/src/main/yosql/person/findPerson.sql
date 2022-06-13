/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

--
-- name: findPerson
-- vendor: Microsoft SQL Server
-- parameters:
--   - name: name
--     type: java.lang.String
--
select *
from persons
where name = :name
;

--
-- name: findPerson
-- vendor: H2
--
select *
from persons
where name = :name
;

--
-- name: findPerson
--
select *
from persons
where name = :name
;
