---
title: basePackageName
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Configuration
categories:
  - Configuration
tags:
  - repositories
  - basePackageName
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

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](../../tooling/maven).

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-maven-plugin</artifactId>
        <configuration>
          <repositories>
            <basePackageName>your.own.domain</basePackageName>
          </repositories>
        </configuration>
      </plugin>
    </plugins>
  </build>
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](../../tooling/gradle).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  repositories {
    basePackageName = "your.own.domain"
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](../../tooling/bazel).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](../../tooling/cli).

```shell
$ yosql --repositories-base-package-name=your.own.domain
```

The shorter form is available as well:

```shell
$ yosql --base-package-name=your.own.domain
```
