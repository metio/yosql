/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

--
--
CREATE TABLE persons (
    id INTEGER,
    name VARCHAR(50)
)
;

CREATE TABLE person_to_company (
    person_id INTEGER,
    company_id INTEGER
)
;
