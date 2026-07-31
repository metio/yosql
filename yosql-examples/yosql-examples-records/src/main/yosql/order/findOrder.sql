/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- name: findOrder
-- returning: single
-- resultRowType: wtf.metio.yosql.example.records.domain.PlacedOrder
-- parameters:
--   - name: id
--     type: java.util.UUID
select id,
       tenant_id,
       state,
       monthly_price,
       created_at,
       activated_at,
       cancelled_at
from placed_order
where id = :id
;
