/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

--
--
CREATE TABLE persons
(
    id   INTEGER,
    name VARCHAR(50)
)
;

-- name: createPersonToCompany
CREATE TABLE person_to_company
(
    person_id  INTEGER,
    company_id INTEGER
)
;
