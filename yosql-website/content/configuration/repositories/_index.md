---
title: Repositories
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Configuration
categories:
  - Configuration
tags:
  - repositories
---

The `repositories` configuration can be used to control how `YoSQL` outputs repositories.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](../tooling/maven).

```xml
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-maven-plugin</artifactId>
        <configuration>
          <repositories>
            <configSetting>configValue</configSetting>
          </repositories>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](../tooling/gradle).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  repositories {
    configSetting = configValue
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](../tooling/bazel).

TODO: info for bazel

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](../tooling/cli).

```shell
$ yosql --repositories_configSetting=configValue
```
