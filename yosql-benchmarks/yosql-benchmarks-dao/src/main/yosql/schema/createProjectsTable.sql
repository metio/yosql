/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

CREATE TABLE IF NOT EXISTS projects (
    pid BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    dateStarted BIGINT
)
;
