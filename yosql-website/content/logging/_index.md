---
title: Logging APIs
date: 2020-04-13
menu: main
---

YoSQL supports various logging frameworks and APIs. We recommend that you select one that is already available in your project. By default, `YoSQL` will not generate any logging statements. Tooling may change the default based on information obtained from your project, e.g. a dependency on slf4j.

- [JUL](./jul): The `java.util.logging` based implementation. It uses the constant `JUL` to identify itself.
- [log4j](./log4j): The `log4j` based implementation. It uses the constant `LOG4J` to identify itself.
- [No-Op](./no-op): The `No-Op` based implementation. It uses the constant `NONE` to identify itself.
- [slf4j](./slf4j): The `slf4j` based implementation. It uses the constant `SLF4J` to identify itself.

## Tooling

Replace `configValue` with the constant value for whatever logging API you want to use in the generated code.

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
            <loggingApi>configValue</loggingApi>
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
    loggingApi = configValue
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](../../../tooling/bazel).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](../../../tooling/cli).

```shell
$ yosql --apis-logging-api=configValue
```

The shorter form is available as well:

```shell
$ yosql --logging-api=configValue
```
