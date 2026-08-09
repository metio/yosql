/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- name: findDocument
-- returning: single
-- resultRowType: wtf.metio.yosql.example.cli.config.domain.Document
-- generateResultRowType: true
select id,
       payload,
       revision
from document
where id = :id
;

-- name: findDocumentColumns
-- returning: single
select id,
       revision
from document
where id = :id
;
