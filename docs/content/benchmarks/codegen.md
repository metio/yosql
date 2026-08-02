---
title: Code Generation
date: 2026-08-02
menu:
  main:
    parent: Benchmarks
categories:
  - Benchmarks
tags:
  - code
  - generation
---

How long `YoSQL` takes to read `.sql` files, parse them, and write the Java — which is time added to
every build of every project that uses it.

## What is measured

Three sizes — **10, 25 and 50 repositories** — in each of five logging configurations:

| Configuration | What it generates |
| --- | --- |
| no logging | The baseline. No logging statements at all. |
| `java.util.logging` | Logging through the JDK's own API. |
| `log4j` | Logging through log4j. |
| `slf4j` | Logging through slf4j. |
| `tinylog` | Logging through tinylog. |

Every repository holds the same eight statements: a stored-procedure call and several of them, a
write and several, a read and several, an update and several.

## The published results

[The results](https://jmh.morethan.io/?sources=https://yosql.projects.metio.wtf/benchmarks/results/yosql-benchmarks-codegen-baseline.json,https://yosql.projects.metio.wtf/benchmarks/results/yosql-benchmarks-codegen-CURRENT.json)
are what a shared GitHub Actions runner measured, and should be read as an order of magnitude rather
than a measurement — see [reading the numbers](../).

What they show, and what holds across machines:

- **Generating logging statements costs more than not generating them.** Expected, since there is
  more code to write.
- **Which logging implementation makes no observable difference.** They all cost about the same.
- **The whole thing is measured in milliseconds**, even for 50 repositories. If your build is slow,
  this is not why.

If code generation genuinely is on your critical path, the way out is not a setting: generate the
repositories in one module and depend on it from the others, so the work happens once.

## Running it

```console
mvn --projects yosql-benchmarks/yosql-benchmarks-codegen --also-make \
  --activate-profiles benchmarks verify
```

The run writes `target/benchmark/yosql-benchmarks-codegen.json`. It takes tens of minutes and wants
a machine with nothing else on it — a laptop compiling something else in another window will produce
numbers that mean nothing.
