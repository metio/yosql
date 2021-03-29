---
title: Benchmarks
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    weight: 130
---

This part of the documentation is intended for **developers** looking for information `YoSQL` itself or its generated code perform in various scenarios.

All benchmark results are computed on a free GitHub Actions account - thanks GitHub! The numbers may vary greatly between runs, depending on how much hardware is available to the runner. The numbers within one run are usually consistent and can be used to roughly measure performance. All available benchmarks can be seen at the bottom of this page. Each benchmark page explains how to run the benchmarks yourself. In case you want to make decision based on the performance of the generated code or the code generation itself, run these benchmarks on your own machine in order to see how well `YoSQL` performs against your actual hardware with actual SQL statements. The [initial setup](/tooling/) is simple enough, so that short 1 day/week/sprint spikes should be possible.

All benchmarks are using [jmh](https://github.com/openjdk/jmh) and are part of the git repository at https://github.com/metio/yosql. Clone the repository if you are planning on running benchmarks yourself.

```shell
$ git clone https://github.com/metio/yosql
```

Benchmarks are only enabled on demand by activating the `benchmarks` profile (`mvn --activate-profiles benchmarks ...`). You are always more than welcome to improve any of the existing benchmarks or even add new ones. All currently available benchmarks make use of the fantastic [jmh-maven-plugin](https://github.com/jhunters/jmh-maven-plugin) and thus require Maven to run. In case you are working on your own persistence implementation for `YoSQL`, use these benchmarks as baseline for your actual implementation to measure the performance of your code.

```shell
# run all benchmarks
$ mvn verify --activate-profiles benchmarks
```
