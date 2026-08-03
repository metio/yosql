---
title: Benchmarks
date: 2026-08-02
menu:
  main:
    weight: 130
---

Two things are measured: how long `YoSQL` takes to generate code, and how much the code it generates
adds on top of JDBC.

- [Code generation](./codegen/) — reading, parsing and generating 10, 25 and 50 repositories, in each
  logging configuration.
- [Database access](./db-access/) — running statements through a generated repository, in each
  logging configuration, and the same statements through [JDBI](https://jdbi.org/) for comparison.

## Reading the numbers

The published results were measured on a **free GitHub Actions runner**, which is shared hardware
with no guarantee of what else is on it. Within a single run they are consistent enough to compare
configurations against each other; between runs, and against your own machine, they are not.

Some of them carry a confidence interval wider than the score. Where that is so, the honest reading
is "these two configurations are indistinguishable here", not "this one is faster".

**Run them yourself before deciding anything.** Your hardware and your statements are the only ones
that matter for your project, and both benchmarks take one command.

```console
git clone https://github.com/metio/yosql
mvn verify --activate-profiles benchmarks
```

The `benchmarks` profile is off by default — a full JMH run takes far longer than the rest of the
build put together, so it is not part of the normal gate. Each module writes its results to
`target/benchmark/*.json`, which [jmh.morethan.io](https://jmh.morethan.io/) renders against the
published baseline.

Benchmarks use [jmh](https://github.com/openjdk/jmh) via the
[jmh-maven-plugin](https://github.com/jhunters/jmh-maven-plugin). Improvements and new scenarios are
welcome.
