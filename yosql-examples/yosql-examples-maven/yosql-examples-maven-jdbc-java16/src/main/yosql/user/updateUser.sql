/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

--
-- name: updateUser
-- repository: wtf.metio.yosql.example.maven.jdbc.java16.persistence.AdminRepository
--
update users
set id = 1
where id = :id
  and name = :name
;
