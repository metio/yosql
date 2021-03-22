---
title: allowedReadPrefixes
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Repositories
categories:
  - Configuration
tags:
  - repositories
  - validation
  - name
  - prefix
  - read
---

Configures which name prefixes are allowed for statements that are reading data from your database. It defaults to `read, select, find, query, lookup, get`.

## Configuration Options

### Option: 'read, select, find, query, lookup, get'

The default value of the `allowedReadPrefixes` configuration option is `read, select, find, query, lookup, get` to allow several commonly used names for reading statements.

### Option: 'something'

Changing the `allowedReadPrefixes` configuration option to `something` only allows names with the prefix `something` to read data.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](../../tooling/maven).

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <configuration>
          <repositories>
            <allowedReadPrefixes>
              <allowedReadPrefix>something</allowedReadPrefix>
            </allowedReadPrefixes>
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
      allowedReadPrefixes = listOf("something")
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](../../tooling/bazel).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](../../tooling/cli).

```shell
$ yosql --repositories-allowed-read-prefixes="something"
```

The shorter form is available as well:

```shell
$ yosql --allowed-read-prefixes="something"
```
