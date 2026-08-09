/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- name: createDocumentTable
-- returning: none
create table if not exists document (
    id      uuid  not null primary key,
    payload jsonb not null,
    plain   json  not null
)
;

-- name: deleteDocuments
-- returning: none
delete from document
;

-- name: insertDocument
-- returning: none
-- parameters:
--   - name: id
--     type: java.util.UUID
--   - name: payload
--     type: java.lang.String
--   - name: plain
--     type: java.lang.String
insert into document (id, payload, plain)
values (:id, cast(:payload as jsonb), cast(:plain as json))
;

-- name: findDocument
-- returning: single
-- resultRowType: wtf.metio.yosql.example.nativeimage.domain.Document
-- generateResultRowType: true
-- parameters:
--   - name: id
--     type: java.util.UUID
select id,
       payload,
       plain
from document
where id = :id
;
