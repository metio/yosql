---
title: apiVersion
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Java
categories:
  - Configuration
tags:
  - Java
  - api
---

The `apiVersion` configuration controls which Java JDK API level should be targeted.

## Configuration Options

### Option: '16'

The default value of the `apiVersion` configuration option is `16`. It is updated alongside the minimum Java version required by `YoSQL`.

### Option: '11'

Changing the `apiVersion` configuration option to `11` will allow generated code to use Java APIs up until version 11 (including).

## Related Options

- [useDiamondOperator](../usediamondoperator/): Controls whether the diamond operator is used in generated code.
- [useFinal](../usefinal/): Controls whether parameters and variables are declared `final`.
- [useGenerics](../usegenerics/): Controls whether generic type parameters are used in generated code.
- [useRecords](../userecords/): Controls whether records are used in generated code.
- [useStreamApi](../usestreamapi/): Controls whether stream API is used in generated code.
- [useTextBlocks](../usetextblocks/): Controls whether text blocks are used in generated code.
- [useVar](../usevar/): Controls whether the `var` keyword is used in generated code.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/).

{{< maven/config/java/apiVersion >}}

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "2021.4.21"
}

yosql {
  java {
    apiVersion.set(17)
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
    apiVersion = 17
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --java-api-version=17
```

The shorter form is available as well:

```shell
$ yosql --api-version=17
```
