/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- name: insertTenant
-- returning: none
-- parameters:
--   - name: id
--     type: java.util.UUID
--   - name: accountId
--     type: java.util.UUID
--   - name: slug
--     type: java.lang.String
--   - name: name
--     type: java.lang.String
--   - name: currency
--     type: java.lang.String
--   - name: timeZone
--     type: java.lang.String
--   - name: language
--     type: java.lang.String
--   - name: createdAt
--     type: java.sql.Timestamp
insert into tenant (id, account_id, slug, name, currency, time_zone, language, created_at)
values (:id, :accountId, :slug, :name, :currency, :timeZone, :language, :createdAt)
;

-- name: insertLedgerEntry
-- returning: none
-- parameters:
--   - name: id
--     type: java.lang.Long
--   - name: tenantId
--     type: java.util.UUID
--   - name: amountCents
--     type: java.lang.Long
--   - name: currency
--     type: java.lang.String
--   - name: reason
--     type: java.lang.String
--   - name: reference
--     type: java.lang.String
--   - name: createdAt
--     type: java.sql.Timestamp
insert into ledger_entry (id, tenant_id, amount_cents, currency, reason, reference, created_at)
values (:id, :tenantId, :amountCents, :currency, :reason, :reference, :createdAt)
;

-- name: insertOrder
-- returning: none
-- parameters:
--   - name: id
--     type: java.util.UUID
--   - name: tenantId
--     type: java.util.UUID
--   - name: state
--     type: java.lang.String
--   - name: monthlyPrice
--     type: java.math.BigDecimal
--   - name: createdAt
--     type: java.sql.Timestamp
--   - name: activatedAt
--     type: java.sql.Timestamp
--   - name: cancelledAt
--     type: java.sql.Timestamp
insert into placed_order (id, tenant_id, state, monthly_price, created_at, activated_at, cancelled_at)
values (:id, :tenantId, :state, :monthlyPrice, :createdAt, :activatedAt, :cancelledAt)
;
