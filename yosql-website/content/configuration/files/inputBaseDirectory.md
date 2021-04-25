---
title: inputBaseDirectory
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Files
categories:
  - Configuration
tags:
  - files
  - directories
  - input
---

Controls the root directory `YoSQL` will use to search for ´.sql` files. The given directory itself and all subdirectories will be searched recursively.

## Configuration Options

### Option: '.'

The default value of the `inputBaseDirectory` configuration option is `.` - the current directory. Note that tooling may change the default input base directory to better reflect a typical project structure used with such a tool.

### Option: 'some/other/directory'

Changing the `inputBaseDirectory` configuration option to `some/other/directory!` configures `YoSQL` to look into the path relative directory `some/other/directory`.

### Option: '/an/absolute/path'

Changing the `inputBaseDirectory` configuration option to `/an/absolute/path!` configures `YoSQL` to look into the absolute directory path `/an/absolute/path`.

## Related Options

- [outputBaseDirectory](../outputbasedirectory/): Controls the output directory for `.java` files.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). By default, the Maven tooling with set `inputBaseDirectory` to `src/main/yosql`.

{{< maven/config/files/inputBaseDirectory >}}

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  files {
    inputBaseDirectory = file("src/main/resources")
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --files-input-base-directory=src/main/resources
```

The shorter form is available as well:

```shell
$ yosql --input-base-directory=src/main/resources
```
