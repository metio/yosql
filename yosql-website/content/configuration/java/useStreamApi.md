---
title: useStreamApi
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Java
categories:
  - Configuration
tags:
  - Java
  - generics
---

The `useStreamApi` configuration controls whether stream API used in generated code. Note that the diamond operator was introduced with **Java 8**.

## Configuration Options

### Option: 'true'

The default value of the `useStreamApi` configuration option is `true` which enables the use of the stream API in generated code.

### Option: 'false'

Changing the `useStreamApi` configuration option to `false` disables the use of the stream API in generated code.

## Related Options

- [apiVersion](../apiversion/): Controls which Java APIs can be used in generated code.
- [useDiamondOperator](../usediamondoperator/): Controls whether the diamond operator is used in generated code.
- [useFinal](../usefinal/): Controls whether parameters and variables are declared `final`.
- [useGenerics](../usegenerics/): Controls whether generic type parameters are used in generated code.
- [useRecords](../userecords/): Controls whether records are used in generated code.
- [useTextBlocks](../usetextblocks/): Controls whether text blocks are used in generated code.
- [useVar](../usevar/): Controls whether the `var` keyword is used in generated code.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/).

{{< maven/config/java/useStreamApi >}}

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "2021.4.21"
}

yosql {
  java {
    useStreamApi.set(false)
  }
}
```

```groovy
plugins {
  id "java"
  id "wtf.metio.yosql" version "2021.4.21"
}

yosql {
  java {
    useStreamApi = false
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --java-use-stream-api=false
```

The shorter form is available as well:

```shell
$ yosql --use-stream-api=false
```
