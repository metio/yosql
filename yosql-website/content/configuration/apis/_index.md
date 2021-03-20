---
title: APIs
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Configuration
categories:
  - Configuration
tags:
  - APIs
---

The `apis` configuration can be used to control which APIs `YoSQL` is using in the generated code.

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
          <apis>
            <configOption>configValue</configOption>
          </apis>
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
  apis {
    configOption = configValue
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
$ yosql --apis-config-option=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```shell
$ yosql --config-option=configValue
```
