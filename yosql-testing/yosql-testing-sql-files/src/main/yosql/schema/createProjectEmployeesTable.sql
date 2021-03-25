/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

--
--
CREATE TABLE IF NOT EXISTS projectEmployees
(
    project_pid     BIGINT REFERENCES projects(pid),
    employee_pid    BIGINT REFERENCES employees(pid),
    CONSTRAINT projectEmployees_pk PRIMARY KEY (project_pid, employee_pid)
)
;
