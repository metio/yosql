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

[Code Generation](https://jmh.morethan.io/?sources=https://yosql.projects.metio.wtf/benchmarks/2021/yosql-benchmarks-codegen.json,https://yosql.projects.metio.wtf/benchmarks/current/yosql-benchmarks-codegen.json): Measures how long it takes to read, parse, and generate 500, 250, 100, and 25 repositories in various configurations.
  - Defaults: Uses the default configuration of `YoSQL`.
  - Logging: Enables the use of the `java.util.logging` API in generated code.
  - Spring: Uses Spring JDBC instead of JDBC in the generated code.

```shell
# run code generation benchmark
$ mvn --projects yosql-benchmarks/yosql-benchmarks-codegen --also-make --activate-profiles benchmarks test
```
