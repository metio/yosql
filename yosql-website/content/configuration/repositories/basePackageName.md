---
title: basePackageName
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Repositories
categories:
  - Configuration
tags:
  - repositories
  - base
  - package
  - name
---

The base package name for the generated code. Defaults to `com.example.persistence`.

## Configuration Options

### Option: 'com.example.persistence'

The default value of the `basePackageName` configuration option is `com.example.persistence`. Setting the option to `com.example.persistence` therefore produces the same code generated as the default configuration without any configuration option set. It produces code similar to this:

```java
package com.example.persistence;

public class SomeRepository {

    // ... rest of generated code

}
```

### Option: 'your.own.domain'

Changing the `basePackageName` configuration option to `your.own.domain` generates the following code instead:

```java
package your.own.domain;

public class SomeRepository {

    // ... rest of generated code (same as above)

}
```

## Related Options

- [allowedCallPrefixes](../allowedcallprefixes/): Controls which method name prefixes are allowed for calling statements.
- [allowedReadPrefixes](../allowedreadprefixes/): Controls which method name prefixes are allowed for reading statements.
- [allowedWritePrefixes](../allowedwriteprefixes/): Controls which method name prefixes are allowed for writing statements.
- [injectConverters](../injectconverters/): Controls whether converters are injected as constructor parameters.
- [validateMethodNamePrefixes](../validatemethodnameprefixes/): Controls whether method name prefixes are validated.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/).

{{< maven/config/repositories/basePackageName >}}

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "2021.4.21"
}

yosql {
  repositories {
    basePackageName.set("your.own.domain")
  }
}
```

```groovy
plugins {
  id "java"
  id "wtf.metio.yosql" version "2021.4.21"
}

yosql {
  repositories {
    basePackageName = "your.own.domain"
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --repositories-base-package-name=your.own.domain
```

The shorter form is available as well:

```shell
$ yosql --base-package-name=your.own.domain
```
