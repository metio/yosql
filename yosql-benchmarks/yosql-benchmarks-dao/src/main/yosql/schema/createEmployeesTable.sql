/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

CREATE TABLE IF NOT EXISTS employees(
    pid BIGINT AUTO_INCREMENT,
    department_pid BIGINT REFERENCES departments(pid),
    name VARCHAR(50),
    surname VARCHAR(50),
    email VARCHAR(50),
    salary BIGINT,
    CONSTRAINT employees_pk PRIMARY KEY(pid, department_pid)
)
;
