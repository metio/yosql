---
title: log4j
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Logging APIs
categories:
  - Loggers
tags:
  - log4j
---

The [log4j](https://logging.apache.org/log4j/2.x/) based implementation for a logging generator. All loggers use the [basePackageName]() as their base name.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](../../../tooling/maven).

```xml
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <configuration>
          <apis>
            <loggingApi>LOG4J</loggingApi>
          </apis>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](../../../tooling/gradle).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  apis {
    loggingApi = LOG4J
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](../../../tooling/bazel).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](../../../tooling/cli).

```shell
$ yosql --apis-logging-api=LOG4J
```

The shorter form is available as well:

```shell
$ yosql --logging-api=LOG4J
```
