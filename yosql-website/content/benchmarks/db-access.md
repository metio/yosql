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

The following benchmarks try to use the fastest code paths available to execute SQL statements.

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

### JDBC

In order to run JDBC benchmarks of `YoSQL` run:

```console
# run JDBC benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-dao --also-make --activate-profiles benchmarks verify
```
The [results](https://jmh.morethan.io/?sources=https://yosql.projects.metio.wtf/benchmarks/results/yosql-benchmarks-dao-baseline.json,https://yosql.projects.metio.wtf/benchmarks/results/yosql-benchmarks-dao-CURRENT.json) are measured in **microseconds**. All available logging implementation are tested with their maximal output configuration in order to gauge how much overhead each implementation causes on top of the no-op implementation without any logging statements.

## Other Solutions

In order to compare a solution entirely based on `YoSQL` against other persistence solutions, the following set of benchmarks was created. None of them use `Yosql` and solely use own native persistence API itself.

### EBean

In order to run benchmarks against EBean run:

```console
# run EBean benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-vs-ebean --also-make --activate-profiles benchmarks verify
```

### JDBI

In order to run benchmarks against JDBI implementation run:

```console
# run JDBI benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-vs-jdbi --also-make --activate-profiles benchmarks verify
```

### jOOQ

In order to run benchmarks against jOOQ implementation run:

```console
# run jOOQ benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-vs-jooq --also-make --activate-profiles benchmarks verify
```

### JPA

In order to run benchmarks against JPA implementation run:

```console
# run JPA benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-vs-jpa --also-make --activate-profiles benchmarks verify
```
