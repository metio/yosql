---
title: No-Op
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Logging APIs
categories:
  - Loggers
tags:
  - no-op
---

The `no-op` implementation for a logging generator. It won't generate any logging statements in your generated code.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](/tooling/maven/).

{{< maven/logging/no-op >}}

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  apis {
    loggingApi = NONE
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --apis-logging-api=NONE
```

The shorter form is available as well:

```shell
$ yosql --logging-api=NONE
```
