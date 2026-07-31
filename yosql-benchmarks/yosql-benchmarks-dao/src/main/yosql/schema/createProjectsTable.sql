/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

CREATE TABLE IF NOT EXISTS projects (
    pid BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    dateStarted BIGINT
)
;
