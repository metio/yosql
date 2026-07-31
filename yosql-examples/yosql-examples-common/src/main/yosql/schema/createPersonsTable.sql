/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
