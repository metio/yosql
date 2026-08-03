/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- name: findTenantsExcept
-- returning: multiple
-- resultRowType: wtf.metio.yosql.example.records.domain.Tenant
-- parameters:
--   - name: excluded
--     type: java.util.List<java.util.UUID>
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
where id not in (:excluded)
  and account_id = :accountId
order by slug
;
