---
title: sqlFilesCharset
date: 2019-09-27T18:51:08+02:00
menu:
  main:
    parent: Files
categories:
  - Configuration
tags:
  - sql
  - files
  - charset
---

The `sqlFilesCharset` option can be used to change the charset `YoSQL` uses to read your `.sql` files. It defaults to `UTF-8`.

## Configuration Options

### Option: 'UTF-8'

The default value of the `sqlFilesCharset` configuration option is `UTF-8` which should work on most systems.

### Option: 'ISO-8859-1'

Changing the `sqlFilesCharset` configuration option to `ISO-8859-1` configures `YoSQL` to use the `ISO-8859-1` charset while reading your `.sql` files.

## Related Options

- [inputBaseDirectory](../inputbasedirectory/): Controls the base directory for `.sql` files.
- [outputBaseDirectory](../outputbasedirectory/): Controls the output directory for `.java` files.
- [skipLines](../skiplines/): Skip lines at the beginning of each `.sql` file.
- [sqlFilesSuffix](../sqlfilessuffix/): Controls the expected file ending when looking for `.sql` files.
- [sqlStatementSeparator](../sqlstatementseparator/): The statement separator to use when parsing `.sql` files.

## Tooling

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/).

{{< maven/config/files/sqlFilesCharset >}}

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/).

```groovy
plugins {
  id("wtf.metio.yosql")
}

yosql {
  files {
      sqlFilesCharset = configValue
  }
}
```

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use YoSQL on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```shell
$ yosql --files-sql-files-charset=configValue
```

The shorter form is available as well:

```shell
$ yosql --sql-files-charset=configValue
```
