/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

CREATE TABLE IF NOT EXISTS departments (
    pid BIGINT PRIMARY KEY AUTO_INCREMENT,
    company_pid BIGINT REFERENCES companies(pid),
    name VARCHAR(50)
)
;
