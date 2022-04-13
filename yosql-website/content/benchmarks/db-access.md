---
title: Database Access
date: 2019-06-16T18:51:48+02:00
menu:
  main:
    parent: Benchmarks
categories:
  - Benchmarks 
tags:
  - database
  - access
---

Each supported [persistence](/persistence/) API has their own performance characteristics. The following benchmarks try to use the fastest code paths available to execute SQL statements.

### Common Scenarios

The common scenarios define what each persistence solution must solve in order to pass the TCK. All benchmarks run through the following benchmarks:

#### Reading Data

- `readComplexRelationship`: Read a complex data relationship.
- `readManyToOneRelation`: Reads the one part of a many-to-one relation.
- `readMultipleEntities`: Read multiple entities in one go.
- `readMultipleEntitiesBasedOnCondition`: Read multiple entities and filter them inside the database.
- `readOneToManyRelation`: Reads the many part of a one-to-many relation.
- `readSingleEntityByPrimaryKey`: Read a single entity using its primary key.

#### Writing Data

- `writeMultipleEntities`: Writes multiple entities into the database.
- `writeSingleEntity`: Writes a new entity into the database.
- `updateOneToManyRelation`: Update the one-to-many relationship part of an entity.
- `updateManyToOneRelation`: Update the many-to-one relationship part of an entity.
- `deleteSingleEntityByPrimaryKey`: Delete a single entity using its primary key.

#### Calling Stored Procedures

- `callStoredProcedure`: Call a single stored procedure.

## YoSQL Implementations

In order to select the best matching persistence API for your project, performance might be taken into consideration. In general, we recommend to use whatever persistence API is already used in your project in order to minimize the number of dependencies.

### EBean

In order to run benchmarks for the  [EBean](/persistence/ebean/) implementation of `YoSQL` run:

```console
# run EBean benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-ebean --also-make --activate-profiles benchmarks verify
```

**WORK IN PROGRESS*: https://github.com/metio/yosql/issues/135

### Fluent JDBC

In order to run benchmarks for the  [Fluent JDBC](/persistence/fluent-jdbc/) implementation of `YoSQL` run:

```console
# run Fluent JDBC benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-fluent-jdbc --also-make --activate-profiles benchmarks verify
```

### JDBC

In order to run benchmarks for the  [JDBC](/persistence/jdbc/) implementation of `YoSQL` run:

```console
# run JDBC benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-jdbc --also-make --activate-profiles benchmarks verify
```
The [results](https://jmh.morethan.io/?sources=https://yosql.projects.metio.wtf/benchmarks/results/yosql-benchmarks-jdbc-baseline.json,https://yosql.projects.metio.wtf/benchmarks/results/yosql-benchmarks-jdbc-CURRENT.json) are measured in **microseconds**. All available logging implementation are tested with their maximal output configuration in order to gauge how much overhead each implementation causes on top of the no-op implementation without any logging statements.

### JDBI

In order to run benchmarks for the  [JDBI](/persistence/jdbi/) implementation of `YoSQL` run:

```console
# run JDBI benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-jdbi --also-make --activate-profiles benchmarks verify
```

**WORK IN PROGRESS*: https://github.com/metio/yosql/issues/136

### jOOQ

In order to run benchmarks for the  [jOOQ](/persistence/jooq/) implementation of `YoSQL` run:

```console
# run jOOQ benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-jooq --also-make --activate-profiles benchmarks verify
```

**WORK IN PROGRESS*: https://github.com/metio/yosql/issues/137

### JPA

In order to run benchmarks for the  [JPA](/persistence/jpa/) implementation of `YoSQL` run:

```console
# run JPA benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-jpa --also-make --activate-profiles benchmarks verify
```

**WORK IN PROGRESS*: https://github.com/metio/yosql/issues/138

### MyBatis

In order to run benchmarks for the  [MyBatis](/persistence/mybatis/) implementation of `YoSQL` run:

```console
# run MyBatis benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-mybatis --also-make --activate-profiles benchmarks verify
```

### Pyranid

In order to run benchmarks for the  [Pyranid](/persistence/pyranid/) implementation of `YoSQL` run:

```console
# run Pyranid benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-pyranid --also-make --activate-profiles benchmarks verify
```

### R2DBC

In order to run benchmarks for the  [R2DBC](/persistence/r2dbc/) implementation of `YoSQL` run:

```console
# run R2DBC benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-r2dbc --also-make --activate-profiles benchmarks verify
```

**WORK IN PROGRESS*: https://github.com/metio/yosql/issues/139

### SansOrm

In order to run benchmarks for the  [SansOrm](/persistence/sansorm/) implementation of `YoSQL` run:

```console
# run SansOrm benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-sansorm --also-make --activate-profiles benchmarks verify
```

### Spring-Data JDBC

In order to run benchmarks for the  [Spring-Data JDBC](/persistence/spring-data-jdbc/) implementation of `YoSQL` run:

```console
# run Spring-Data JDBC benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-spring-data-jdbc --also-make --activate-profiles benchmarks verify
```

**WORK IN PROGRESS*: https://github.com/metio/yosql/issues/140

### Spring-Data JPA

In order to run benchmarks for the  [Spring-Data JPA](/persistence/spring-data-jpa/) implementation of `YoSQL` run:

```console
# run Spring-Data JPA benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-spring-data-jpa --also-make --activate-profiles benchmarks verify
```

### Spring-Data R2DBC

In order to run benchmarks for the  [Spring-Data R2DBC](/persistence/spring-data-r2dbc/) implementation of `YoSQL` run:

```console
# run Spring-Data JDBC benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-spring-data-r2dbc --also-make --activate-profiles benchmarks verify
```

### Spring JDBC

In order to run benchmarks for the  [Spring JDBC](/persistence/spring-jdbc/) implementation of `YoSQL` run:

```console
# run Spring JDBC benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-spring-jdbc --also-make --activate-profiles benchmarks verify
```

**WORK IN PROGRESS*: https://github.com/metio/yosql/issues/141

## Other Solutions

In order to compare a solution entirely based on `YoSQL` against other persistence solutions, the following set of benchmarks was created. None of them use `Yosql` and solely use own native persistence API itself.

### EBean

In order to run benchmarks for the  [EBean](/persistence/ebean/) *only* implementation run:

```console
# run EBean benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-vs-ebean --also-make --activate-profiles benchmarks verify
```

### JDBI

In order to run benchmarks for the  [JDBI](/persistence/jdbi/) *only* implementation run:

```console
# run JDBI benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-vs-jdbi --also-make --activate-profiles benchmarks verify
```

### jOOQ

In order to run benchmarks for the  [jOOQ](/persistence/jooq/) *only* implementation run:

```console
# run jOOQ benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-vs-jooq --also-make --activate-profiles benchmarks verify
```

### JPA

In order to run benchmarks for the  [JPA](/persistence/jpa/) *only* implementation run:

```console
# run JPA benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-vs-jpa --also-make --activate-profiles benchmarks verify
```
