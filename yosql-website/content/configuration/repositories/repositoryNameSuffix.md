---
title: repositoryNameSuffix
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Repositories
categories:
  - Configuration
tags:
  - repositories
  - name
---

The suffix to append to repository names. In case the repository name already contains the configured suffix, it will not be added twice.

## Configuration Options

### Option: 'Repository'

The default value of the `repositoryNameSuffix` configuration option is `Repository`. Setting the option to `Repository` therefore produces the same code generated as the default configuration without any configuration option set. It produces code similar to this:

```java
package com.example.persistence;

public class SomeRepository {

    // ... rest of generated code

}
```

### Option: 'Repo'

Changing the `repositoryNameSuffix` configuration option to `Repo` generates the following code instead:

```java
package com.example.persistence;

public class SomeRepo {

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

{{< maven/config/repositories/repositoryNameSuffix >}}

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "2021.4.21"
}

yosql {
  repositories {
    repositoryNameSuffix.set("Repo")
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
    repositoryNameSuffix = "Repo"
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --repositories-repository-name-suffix=Repo
```

The shorter form is available as well:

```shell
$ yosql --repository-name-suffix=Repo
```
