/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

-- name: findLedgerEntries
-- returning: multiple
-- resultRowType: wtf.metio.yosql.example.records.domain.LedgerEntry
-- parameters:
--   - name: tenantId
--     type: java.util.UUID
select id,
       amount_cents as minor_units,
       currency,
       reason,
       reference,
       created_at as at
from ledger_entry
where tenant_id = :tenantId
order by id
;
