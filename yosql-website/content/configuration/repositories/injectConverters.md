---
title: injectConverters
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Repositories
categories:
  - Configuration
tags:
  - repositories
  - converters
  - dependency injection
---

Toggles whether converters are exposed as constructor parameters and created by `YoSQL` using the default constructor of a converter. Defaults to `false`.

## Configuration Options

### Option: 'false'

The default value of the `injectConverters` configuration option is `false`. Setting the option to `false` therefore produces the same code generated as the default configuration without any configuration option set. It produces code similar to this:

```java
package com.example.persistence;

public class SomeRepository {

    private final DataSource dataSource;

    private final ToResultRowConverter resultRow;

    public SomeRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
        this.resultRow = new ToResultRowConverter();
    }
    
    // ... rest of generated code

}
```

### Option: 'true'

Changing the `injectConverters` configuration option to `true` generates the following code instead:

```java
package your.own.domain;

public class SomeRepository {

    private final DataSource dataSource;

    private final ToResultRowConverter resultRow;

    public SomeRepository(final DataSource dataSource, final ToResultRowConverter resultRow) {
        this.dataSource = dataSource;
        this.resultRow = resultRow;
    }

    // ... rest of generated code
    
}
```

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](/tooling/maven/).

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <configuration>
          <repositories>
            <injectConverters>true</injectConverters>
          </repositories>
        </configuration>
      </plugin>
    </plugins>
  </build>
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  repositories {
      injectConverters = true
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --repositories-inject-converters=true
```

The shorter form is available as well:

```shell
$ yosql --inject-converters=true
```
