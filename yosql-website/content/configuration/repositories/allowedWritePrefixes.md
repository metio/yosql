---
title: allowedWritePrefixes
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
  - write
---

Configures which name prefixes are allowed for statements that are writing data to your database. It defaults to `update, insert, delete, create, write, add, remove, merge, drop`.

## Configuration Options

### Option: 'update, insert, delete, create, write, add, remove, merge, drop'

The default value of the `allowedWritePrefixes` configuration option is `update, insert, delete, create, write, add, remove, merge, drop` to allow several commonly used names for writing statements.

### Option: 'something'

Changing the `allowedWritePrefixes` configuration option to `something` only allows names with the prefix `something` to write data.

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
            <allowedWritePrefixes>
              <allowedWritePrefix>something</allowedWritePrefix>
            </allowedWritePrefixes>
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
      allowedWritePrefixes = listOf("something")
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](../../tooling/bazel).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](../../tooling/cli).

```shell
$ yosql --repositories-allowed-write-prefixes="something"
```

The shorter form is available as well:

```shell
$ yosql --allowed-write-prefixes="something"
```
