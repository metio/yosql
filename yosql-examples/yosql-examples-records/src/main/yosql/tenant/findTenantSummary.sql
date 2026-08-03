/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- name: findTenantSummary
-- returning: multiple
-- resultRowType: wtf.metio.yosql.example.records.domain.TenantSummary
-- generateResultRowType: true
-- parameters:
--   - name: accountId
--     type: java.util.UUID
select id,
       slug,
       created_at
from tenant
where account_id = :accountId
order by slug
;
