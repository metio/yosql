---
title: Benchmarks
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    weight: 130
---

The following benchmark results are computed on a free GitHub Actions account - thanks GitHub! The numbers may vary greatly between runs, depending on how much hardware is available to the runner. The numbers within one run are usually consistent and can be used to roughly measure performance.

- [Common Scenarios](https://jmh.morethan.io/?sources=https://yosql.projects.metio.wtf/benchmarks/2021/yosql-benchmarks-common-scenarios.json,https://yosql.projects.metio.wtf/benchmarks/current/yosql-benchmarks-common-scenarios.json): Measures how much time commonly used database operations require to complete.
    - Read a single entity based on its primary key.
    - Read multiple entities based on some condition.
    - Write a single entity.
- [JDBC](https://jmh.morethan.io/?sources=https://yosql.projects.metio.wtf/benchmarks/2021/yosql-benchmarks-jdbc.json,https://yosql.projects.metio.wtf/benchmarks/current/yosql-benchmarks-jdbc.json): Measures how long it takes to complete various scenarios using the JDBC API.
    - Read data from a database.
    - Write data into a database.
    - Perform schema manipulation.

## Running Benchmarks

In case you want to run the benchmarks yourself, do this:

```shell
# run common scenario benchmarks with the best performing YoSQL implementation
$ mvn --projects yosql-benchmarks/yosql-benchmarks-common-scenarios --also-make --activate-profiles benchmarks test

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
