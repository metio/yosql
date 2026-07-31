/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

-- name: createTenantTable
-- returning: none
create table tenant (
    id         uuid         not null primary key,
    account_id uuid         not null,
    slug       varchar(64)  not null,
    name       varchar(255) not null,
    currency   varchar(3)   not null,
    time_zone  varchar(64)  not null,
    language   varchar(8)   not null,
    created_at timestamp with time zone not null
)
;

-- name: createLedgerEntryTable
-- returning: none
create table ledger_entry (
    id           bigint       not null primary key,
    tenant_id    uuid         not null,
    amount_cents bigint       not null,
    currency     varchar(3)   not null,
    reason       varchar(32)  not null,
    reference    varchar(255) not null,
    created_at   timestamp with time zone not null
)
;

-- name: createOrderTable
-- returning: none
create table placed_order (
    id            uuid          not null primary key,
    tenant_id     uuid          not null,
    state         varchar(32)   not null,
    monthly_price numeric(12,2) not null,
    created_at    timestamp with time zone not null,
    activated_at  timestamp with time zone,
    cancelled_at  timestamp with time zone
)
;
