---
title: Annotations
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Configuration
categories:
  - Configuration
tags:
  - annotations
---

The `annotations` configuration can be used to control how `YoSQL` outputs annotations.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation 
for Maven](../../tooling/maven).

```xml
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <configuration>
          <annotations>
            <configOption>configValue</configOption>
          </annotations>
        </configuration>
      </plugin>
      ...
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
  annotations {
    configOption = configValue
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for 
Bazel](../../tooling/bazel).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](../../tooling/cli).

```shell
$ yosql --annotations-config-option=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```shell
$ yosql --config-option=configValue
```
