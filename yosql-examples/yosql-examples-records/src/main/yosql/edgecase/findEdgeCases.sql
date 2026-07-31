/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

-- name: createEdgeCaseTable
-- returning: none
create table edge_case (
    id          uuid        not null primary key,
    count_value bigint,
    state       varchar(32) not null
)
;

-- name: insertEdgeCase
-- returning: none
-- parameters:
--   - name: id
--     type: java.util.UUID
--   - name: countValue
--     type: java.lang.Long
--   - name: state
--     type: java.lang.String
insert into edge_case (id, count_value, state)
values (:id, :countValue, :state)
;

-- name: findSample
-- returning: single
-- resultRowType: wtf.metio.yosql.example.records.domain.Sample
-- parameters:
--   - name: id
--     type: java.util.UUID
select id,
       count_value
from edge_case
where id = :id
;

-- name: findStateHolder
-- returning: single
-- resultRowType: wtf.metio.yosql.example.records.domain.StateHolder
-- parameters:
--   - name: id
--     type: java.util.UUID
select id,
       state
from edge_case
where id = :id
;
