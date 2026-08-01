/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- name: createRegistrationTable
-- returning: none
create table registration (
    tenant_id     uuid        not null primary key,
    slug          varchar(64) not null,
    balance       bigint      not null,
    registered_at timestamp with time zone not null
)
;

-- name: insertRegistration
-- returning: none
-- parameters:
--   - name: tenantId
--     type: java.util.UUID
--   - name: slug
--     type: java.lang.String
--   - name: balance
--     type: java.lang.Long
--   - name: registeredAt
--     type: java.sql.Timestamp
insert into registration (tenant_id, slug, balance, registered_at)
values (:tenantId, :slug, :balance, :registeredAt)
;

-- name: insertTypedRegistration
-- returning: none
-- parameters:
--   - name: tenantId
--     type: wtf.metio.yosql.example.records.domain.TenantId
--   - name: slug
--     type: wtf.metio.yosql.example.records.domain.Slug
--   - name: balance
--     type: wtf.metio.yosql.example.records.domain.Cents
--   - name: registeredAt
--     type: java.time.Instant
insert into registration (tenant_id, slug, balance, registered_at)
values (:tenantId, :slug, :balance, :registeredAt)
;

-- name: findRegistration
-- returning: single
-- resultRowType: wtf.metio.yosql.example.records.domain.Registration
-- parameters:
--   - name: tenantId
--     type: java.util.UUID
select tenant_id,
       slug,
       balance,
       registered_at
from registration
where tenant_id = :tenantId
;

-- name: countRegistrations
-- type: reading
-- returning: single
-- resultRowType: java.lang.Long
select count(*)
from registration
;

-- name: findSlugText
-- returning: single
-- resultRowType: java.lang.String
-- parameters:
--   - name: tenantId
--     type: wtf.metio.yosql.example.records.domain.TenantId
select slug
from registration
where tenant_id = :tenantId
;

-- name: findTenantIdentity
-- returning: single
-- resultRowType: wtf.metio.yosql.example.records.domain.TenantId
-- parameters:
--   - name: slug
--     type: wtf.metio.yosql.example.records.domain.Slug
select tenant_id
from registration
where slug = :slug
;

-- name: findMissingBalance
-- returning: single
-- resultRowType: java.lang.Long
-- parameters:
--   - name: tenantId
--     type: wtf.metio.yosql.example.records.domain.TenantId
select nullif(balance, balance)
from registration
where tenant_id = :tenantId
;
