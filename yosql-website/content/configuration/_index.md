---
title: Configuration
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    weight: 110
---

This part of the documentation is intended for **developers** looking for information on the various configuration options that `YoSql` provides.

By default, `YoSQL` generates code that uses all features of the latest supported Java release without using any non-JDK classes. In case you don't want to use a particular feature, use the relevant configuration option to disable it. We recommend to start with an empty configuration and adapting that to your needs, using as many configuration options as required.

Depending on your tooling, `YoSQL` can be configured as explained in the following sections.

## Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](../tooling/maven).

```xml
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <configuration>
          <configGroup>
            <configOption>configValue</configOption>
          </configGroup>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
```

## Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](../tooling/gradle).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  configGroup {
    configOption = configValue
  }
}
```

## Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](../tooling/bazel).

TODO: info for bazel

## CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](../tooling/cli).

```shell
$ yosql --config_group_config_option=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```shell
$ yosql --config_option=configValue
```
