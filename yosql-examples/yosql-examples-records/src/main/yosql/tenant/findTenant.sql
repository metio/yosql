/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- name: findTenant
-- returning: single
-- resultRowType: wtf.metio.yosql.example.records.domain.Tenant
-- parameters:
--   - name: id
--     type: java.util.UUID
select id,
       account_id,
       slug,
       name,
       currency,
       time_zone,
       language,
       created_at
from tenant
where id = :id
;

-- name: findTenantsByAccount
-- returning: multiple
-- resultRowType: wtf.metio.yosql.example.records.domain.Tenant
-- parameters:
--   - name: accountId
--     type: java.util.UUID
select id,
       account_id,
       slug,
       name,
       currency,
       time_zone,
       language,
       created_at
from tenant
where account_id = :accountId
order by slug
;
