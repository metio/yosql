/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

--
--
CREATE TABLE IF NOT EXISTS employees
(
    pid
    BIGINT
    AUTO_INCREMENT,
    department_pid
    BIGINT
    REFERENCES
    departments
(
    pid
),
    name VARCHAR
(
    50
),
    surname VARCHAR
(
    50
),
    email VARCHAR
(
    50
),
    salary BIGINT,
    CONSTRAINT employees_pk PRIMARY KEY
(
    pid,
    department_pid
)
    )
;
