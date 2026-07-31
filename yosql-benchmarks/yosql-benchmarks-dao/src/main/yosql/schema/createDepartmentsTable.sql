/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

CREATE TABLE IF NOT EXISTS departments (
    pid BIGINT PRIMARY KEY AUTO_INCREMENT,
    company_pid BIGINT REFERENCES companies(pid),
    name VARCHAR(50)
)
;
