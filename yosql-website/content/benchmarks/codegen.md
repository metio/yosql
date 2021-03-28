---
title: Code Generation
date: 2019-06-16T18:51:48+02:00
menu:
  main:
    parent: Benchmarks
categories:
  - Benchmarks 
tags:
  - code
  - generation
---

[Code Generation](https://jmh.morethan.io/?sources=https://yosql.projects.metio.wtf/benchmarks/2021/yosql-benchmarks-codegen.json,https://yosql.projects.metio.wtf/benchmarks/current/yosql-benchmarks-codegen.json): Measures how long it takes to read, parse, and generate 50, 25, and 10 repositories in various configurations.
  - JDBC without logging: Generates code using the JDBC API without any logging statements. Use these benchmarks as a baseline for benchmarks.
  - JDBC with JUL: Enables the use of the `java.util.logging` API in generated code.
  - JDBC with log4j: Enables the use of the `log4j` API in generated code.
  - JDBC with slf4j: Enables the use of the `slf4j` API in generated code.
  - JDBC with tinylog: Enables the use of the `tinylog` API in generated code.

```shell
# run code generation benchmark
$ mvn --projects yosql-benchmarks/yosql-benchmarks-codegen --also-make --activate-profiles benchmarks verify
```

The above command will execute the four configurations mentioned at the top of this page. Each configuration uses repositories that contain:

- Call to a stored procedure.
- Multiple calls to a stored procedure.
- Write of an entity.
- Multiple writes of an entity.
- Read of an entity.
- Multiple reads of an entity.
- Update of an entity.
- Multiple updates to an entity.

Comparing code to benchmark, we are expecting that generating any kind of logging statement takes slightly longer than not generating any logging statements at all. Amongst the logging implementation, no observable performance difference can be measured. In case you are concerned about the total time it takes `YoSQL` to generate code, disabling logging statements will make a difference, however the code generation process time can be measures in milliseconds even for very large sample sizes, thus you should probably look somewhere else to improve the total performance of your build. In order to avoid generating code altogether, consider creating repositories inside one module and depend on it in other modules.
