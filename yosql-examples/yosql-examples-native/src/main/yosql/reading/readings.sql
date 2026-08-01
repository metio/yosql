/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

-- name: createReadingTable
-- returning: none
create table if not exists reading (
    id          uuid           not null primary key,
    sensor_id   varchar(64)    not null,
    level       varchar(16)    not null,
    amount      numeric(12, 3) not null,
    unit        varchar(16)    not null,
    recorded_at timestamptz    not null,
    cleared_at  timestamptz
)
;

-- name: deleteReadings
-- returning: none
delete from reading
;

-- name: insertReading
-- returning: none
-- parameters:
--   - name: id
--     type: java.util.UUID
--   - name: sensorId
--     type: java.lang.String
--   - name: level
--     type: java.lang.String
--   - name: amount
--     type: java.math.BigDecimal
--   - name: unit
--     type: java.lang.String
--   - name: recordedAt
--     type: java.sql.Timestamp
--   - name: clearedAt
--     type: java.sql.Timestamp
insert into reading (id, sensor_id, level, amount, unit, recorded_at, cleared_at)
values (:id, :sensorId, :level, :amount, :unit, :recordedAt, :clearedAt)
;

-- name: findReading
-- returning: single
-- resultRowType: wtf.metio.yosql.example.nativeimage.domain.Reading
-- parameters:
--   - name: id
--     type: java.util.UUID
select id,
       sensor_id,
       level,
       amount,
       unit,
       recorded_at,
       cleared_at
from reading
where id = :id
;

-- name: findReadingsBySensor
-- returning: multiple
-- resultRowType: wtf.metio.yosql.example.nativeimage.domain.Reading
-- parameters:
--   - name: sensorId
--     type: java.lang.String
select id,
       sensor_id,
       level,
       amount,
       unit,
       recorded_at,
       cleared_at
from reading
where sensor_id = :sensorId
order by recorded_at
;

-- name: insertReadingReturningId
-- type: reading
-- returning: single
-- resultRowType: wtf.metio.yosql.example.nativeimage.domain.ReadingId
-- parameters:
--   - name: id
--     type: java.util.UUID
--   - name: sensorId
--     type: java.lang.String
--   - name: level
--     type: java.lang.String
--   - name: amount
--     type: java.math.BigDecimal
--   - name: unit
--     type: java.lang.String
--   - name: recordedAt
--     type: java.sql.Timestamp
insert into reading (id, sensor_id, level, amount, unit, recorded_at)
values (:id, :sensorId, :level, :amount, :unit, :recordedAt)
returning id
;
