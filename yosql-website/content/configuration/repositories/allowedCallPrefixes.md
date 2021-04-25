---
title: allowedCallPrefixes
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Repositories
categories:
  - Configuration
tags:
  - repositories
  - validation
  - name
  - prefix
  - procedure
  - call
---

Configures which name prefixes are allowed for statements that are calling stored procedures. It defaults to `call, execute, evaluate, eval`.

## Configuration Options

### Option: 'call, execute, evaluate, eval'

The default value of the `allowedCallPrefixes` configuration option is `false` which disables the validation of names according to your configured prefixes.

### Option: 'do'

Changing the `allowedCallPrefixes` configuration option to `do` only allows names with the prefix `do` to call stored procedures.

## Related Options

- [allowedReadPrefixes](../allowedreadprefixes/): Controls which method name prefixes are allowed for reading statements.
- [allowedWritePrefixes](../allowedwriteprefixes/): Controls which method name prefixes are allowed for writing statements.
- [basePackageName](../basepackagename/): Controls the base package name for generated repositories.
- [injectConverters](../injectconverters/): Controls whether converters are injected as constructor parameters.
- [validateMethodNamePrefixes](../validatemethodnameprefixes/): Controls whether method name prefixes are validated.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/).

{{< maven/config/repositories/allowedCallPrefixes >}}

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "2021.4.21"
}

yosql {
  repositories {
      allowedCallPrefixes = listOf("call", "do")
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --repositories-allowed-call-prefixes="call, do"
```

The shorter form is available as well:

```shell
$ yosql --allowed-call-prefixes="call, do"
```
