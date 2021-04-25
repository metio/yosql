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

- [inputBaseDirectory](../inputbasedirectory/): Controls the base directory for `.sql` files.
- [outputBaseDirectory](../outputbasedirectory/): Controls the output directory for `.java` files.
- [skipLines](../skiplines/): Skip lines at the beginning of each `.sql` file.
- [sqlFilesCharset](../sqlfilescharset/): Controls the charset used to read `.sql` files.
- [sqlStatementSeparator](../sqlstatementseparator/): The statement separator to use when parsing `.sql` files.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/).

{{< maven/config/files/sqlFilesSuffix >}}

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

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --files-sql-files-suffix=configValue
```

The shorter form is available as well:

```shell
$ yosql --sql-files-suffix=configValue
```
