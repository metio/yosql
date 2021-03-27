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

Each supported [persistence](/persistence/) API has their own performance characteristics. The following benchmark try to use the fastest code paths available to execute SQL statements.

## YoSQL Implementations

In order to select the best matching persistence API for your project, performance might be taken into consideration. In general, we recommend to use whatever persistence API is already used in your project in order to minimize the number of dependencies.

### EBean


```shell
# run EBean benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-ebean --also-make --activate-profiles benchmarks test
```

### JDBC

- [JDBC](https://jmh.morethan.io/?sources=https://yosql.projects.metio.wtf/benchmarks/2021/yosql-benchmarks-jdbc.json,https://yosql.projects.metio.wtf/benchmarks/current/yosql-benchmarks-jdbc.json): Measures performance timings for the JDBC API.
  - Read data from a database.
  - Write data into a database.
  - Perform schema manipulation.

```shell
# run EBean benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-ebean --also-make --activate-profiles benchmarks test

# run JDBC benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-jdbc --also-make --activate-profiles benchmarks test

# run JDBI benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-jdbi --also-make --activate-profiles benchmarks test

# run jOOQ benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-jooq --also-make --activate-profiles benchmarks test

# run JPA benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-jpa --also-make --activate-profiles benchmarks test

# run R2DBC benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-r2dbc --also-make --activate-profiles benchmarks test

# run Spring-Data JDBC benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-spring-data-jdbc --also-make --activate-profiles benchmarks test

# run Spring JDBC benchmarks
$ mvn --projects yosql-benchmarks/yosql-benchmarks-spring-jdbc --also-make --activate-profiles benchmarks test
```

## Shootout

In order to compare a solution entirely based on `YoSQL` against other persistence solutions, the following set of benchmarks was created.

### Common Scenarios

```shell
# run common scenario benchmarks with the best performing YoSQL implementation
$ mvn --projects yosql-benchmarks/yosql-benchmarks-common-scenarios --also-make --activate-profiles benchmarks test
```

The common scenarios define what each persistence solution must solve in order to pass our TCK. The benchmark results for the reference implementation using `YoSQL` can be found [here](https://jmh.morethan.io/?sources=https://yosql.projects.metio.wtf/benchmarks/2021/yosql-benchmarks-common-scenarios.json,https://yosql.projects.metio.wtf/benchmarks/current/yosql-benchmarks-common-scenarios.json). Those results can be seen as a baseline for other implementations. The common scenarios are:

#### Reading Data


#### Writing Data

- writeMultipleEntities
- writeSingleEntity
- updateOneToManyRelation
- updateManyToOneRelation
- deleteSingleEntityByPrimaryKey

#### Calling Stored Procedures

- `callStoredProcedure`: Call a single stored procedure.

### EBean

TODO/TBD

### JDBI

TODO/TBD

### jOOQ

TODO/TBD

### JPA

TODO/TBD
