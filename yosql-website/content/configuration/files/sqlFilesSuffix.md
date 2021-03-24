---
title: sqlFilesSuffix
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Files
categories:
  - Configuration
tags:
  - sql
  - files
  - suffix
---

The `sqlFilesSuffix` option can be used to change which file extension is expected by `YoSQL` while searching for your `.sql` files. It defaults to `.sql`.

## Configuration Options

### Option: '.sql'

The default value of the `sqlFilesSuffix` configuration option is `.sql`. It matches all files that end with `.sql`.

### Option: '.other'

Changing the `sqlFilesSuffix` configuration option to `.other` configures `YoSQL` look for files that end in `.other`.

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
            <sqlFilesSuffix>configValue</sqlFilesSuffix>
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
      sqlFilesSuffix = configValue
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
$ yosql --files-sql-files-suffix=configValue
```

The shorter form is available as well:

```shell
$ yosql --sql-files-suffix=configValue
```
