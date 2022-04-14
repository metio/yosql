---
title: loggingApi
date: 2022-04-14
menu:
  main:
    parent: Api
categories:
  - Configuration
tags:
  - api
---

The logging API to use.

## Configuration Options

### Option: 'NONE'

The default `no-op` implementation for a logging generator. It won't generate any logging statements in your generated code.

### Option: 'JUL'

The `java.util.logging` based implementation for a logging generator. The generated code does not require any external non-JDK classes. All loggers use the [basePackageName](/configuration/repositories/basepackagename/) as their base name.

### Option: 'LOG4J'

The [log4j](https://logging.apache.org/log4j/2.x/) based implementation for a logging generator. All loggers use the [basePackageName](/configuration/repositories/basepackagename/) as their base name.

### Option: 'SLF4J'

The [slf4j](https://www.slf4j.org/) based implementation for a logging generator. All loggers use the [basePackageName](/configuration/repositories/basepackagename/) as their base name.

### Option: 'TINYLOG'

The [Tinylog](https://tinylog.org/v2/) based implementation for a logging generator.

## Related Options

- [annotationApi](../annotationapi/): The annotation API to use.
- [daoApi](../daoapi/): The persistence API to use.

## Tooling

### Ant

In order to use `YoSQL` together with [Ant](https://ant.apache.org/), take a look at the tooling [documentation for Ant](/tooling/ant/).

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use `YoSQL` on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```console
$ yosql --api-logging-api=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```console
$ yosql --logging-api=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The `loggingApi` setting can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "0.0.0-SNAPSHOT"
}

yosql {
  api {
    loggingApi.set(configValue)
  }
}
```

or in Groovy syntax like this:

```groovy
plugins {
  id "java"
  id "wtf.metio.yosql" version "0.0.0-SNAPSHOT"
}

yosql {
  api {
    loggingApi = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The `loggingApi` setting can be configured using Maven like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>wtf.metio.yosql</groupId>
      <artifactId>yosql-tooling-maven</artifactId>
      <version>0.0.0-SNAPSHOT</version>
      <configuration>
        <api>
          <loggingApi>configValue</loggingApi>
        </api>
      </configuration>
    </plugin>
  </plugins>
</build>
```
