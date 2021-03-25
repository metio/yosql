---
title: Benchmarks
date: 2020-04-13
menu:
  main:
    weight: 130
---

The following benchmark results are computed on a free GitHub Actions account - thanks GitHub! The numbers may vary greatly between runs, depending on how much hardware is available to the runner. The numbers within one run are usually consistent and can be used to roughly measure performance.

- [Code Generation](https://jmh.morethan.io/?sources=https://yosql.projects.metio.wtf/benchmarks/2021/yosql-benchmarks-codegen.json,https://yosql.projects.metio.wtf/benchmarks/current/yosql-benchmarks-codegen.json): Measures how long it takes to read, parse, and generate 500, 250, 100, and 25 repositories in various configurations.
    - Defaults: Uses the default configuration of `YoSQL`.
    - Logging: Enables the use of the `java.util.logging` API in generated code.
    - Spring: Uses Spring JDBC instead of JDBC in the generated code.
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
# run code generation benchmark
$ mvn --projects yosql-benchmarks/yosql-benchmarks-codegen --also-make --activate-profiles benchmarks test

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
