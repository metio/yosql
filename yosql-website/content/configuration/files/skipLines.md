---
title: skipLines
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Files
categories:
  - Configuration
tags:
  - sql
  - files
  - skip
---

The `skipLines` option can be used to skip a certain number of lines in each `.sql`, e.g. a license header that is present in each file. It defaults to `0` and thus does not skip any lines.

## Configuration Options

### Option: '0'

The default value of the `skipLines` configuration option is `0` - which does not skip any lines.

### Option: '5'

Changing the `skipLines` configuration option to `5` configures `YoSQL` skip the first 5 lines in each `.sql` file in encounters.

## Related Options

- [inputBaseDirectory](/configuration/files/inputbasedirectory/): Controls the base directory for `.sql` files.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation
for Maven](/tooling/maven/).

```xml
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>wtf.metio.yosql</groupId>
        <artifactId>yosql-tooling-maven</artifactId>
        <configuration>
          <files>
            <skipLines>configValue</skipLines>
          </files>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  files {
      skipLines = configValue
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for
Bazel](/tooling/bazel/).

TODO: info for bazel

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --files-skip-lines=configValue
```

The shorter form is available as well:

```shell
$ yosql --skip-lines=configValue
```
