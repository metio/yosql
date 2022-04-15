---
title: maxThreads
date: 2022-04-15
menu:
  main:
    parent: Resources
categories:
  - Configuration
tags:
  - resources
  - threads
---

Controls how many threads are used during code generation.

The maximum number will be capped to the number of available CPU cores of your system.

## Configuration Options

### Option: '1'

The default value of the `maxThreads` configuration option is `1` which uses one thread to generate code.

### Option: '123'

Changing the `maxThreads` configuration option to `123` will use the available number of CPU cores in your system or 123 threads, depending on which is lower.


## Tooling

### Ant

In order to use `YoSQL` together with [Ant](https://ant.apache.org/), take a look at the tooling [documentation for Ant](/tooling/ant/).

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use `YoSQL` on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```console
$ yosql --resources-max-threads=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```console
$ yosql --max-threads=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The `maxThreads` setting can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "0.0.0-SNAPSHOT"
}

yosql {
  resources {
    maxThreads.set(configValue)
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
  resources {
    maxThreads = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The `maxThreads` setting can be configured using Maven like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>wtf.metio.yosql</groupId>
      <artifactId>yosql-tooling-maven</artifactId>
      <version>0.0.0-SNAPSHOT</version>
      <configuration>
        <resources>
          <maxThreads>configValue</maxThreads>
        </resources>
      </configuration>
    </plugin>
  </plugins>
</build>
```
