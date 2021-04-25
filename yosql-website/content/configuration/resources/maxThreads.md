---
title: maxThreads
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Resources
categories:
  - Configuration
tags:
  - threads
---

The `maxThreads` configuration controls how many threads are used during code generation. The maximum number will be capped to the number of available CPU cores of your system.

## Configuration Options

### Option: '1'

The default value of the `maxThreads` configuration option is `1` which uses one thread to generate code.

### Option: '123'

Changing the `maxThreads` configuration option to `123` will use the available number of CPU cores in your system or 123 threads, depending on which is lower.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/).

{{< maven/config/resources/maxThreads >}}

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "2021.4.21"
}

yosql {
  resources {
    maxThreads.set(123)
  }
}
```

```groovy
plugins {
  id "java"
  id "wtf.metio.yosql" version "2021.4.21"
}

yosql {
  resources {
    maxThreads = 123
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --resources-max-threads=configValue
```

The shorter form is available as well:

```shell
$ yosql --max-threads=configValue
```
