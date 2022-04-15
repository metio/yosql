---
title: sqlFilesSuffix
date: 2022-04-15
menu:
  main:
    parent: Files
categories:
  - Configuration
tags:
  - files
---

The file ending to use while searching for SQL files.

## Configuration Options

### Option: '.sql'

The default value of the `sqlFilesSuffix` configuration option is `.sql`. It matches all files that end with `.sql`.

### Option: '.other'

Changing the `sqlFilesSuffix` configuration option to `.other` configures `YoSQL` look for files that end in `.other`.

## Related Options

- [inputBaseDirectory](../inputbasedirectory/): The input directory for the user written SQL files.
- [outputBaseDirectory](../outputbasedirectory/): The output directory for the generated classes.
- [skipLines](../skiplines/): The number of lines to skip in each file (e.g. a copyright header).
- [sqlFilesCharset](../sqlfilescharset/): The charset to use while reading .sql files.
- [sqlStatementSeparator](../sqlstatementseparator/): The separator to split SQL statements inside a single .sql file.

## Tooling

### Ant

In order to use `YoSQL` together with [Ant](https://ant.apache.org/), take a look at the tooling [documentation for Ant](/tooling/ant/).

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use `YoSQL` on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```console
$ yosql --files-sql-files-suffix=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```console
$ yosql --sql-files-suffix=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The `sqlFilesSuffix` setting can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "0.0.0-SNAPSHOT"
}

yosql {
  files {
    sqlFilesSuffix.set(configValue)
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
  files {
    sqlFilesSuffix = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The `sqlFilesSuffix` setting can be configured using Maven like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>wtf.metio.yosql</groupId>
      <artifactId>yosql-tooling-maven</artifactId>
      <version>0.0.0-SNAPSHOT</version>
      <configuration>
        <files>
          <sqlFilesSuffix>configValue</sqlFilesSuffix>
        </files>
      </configuration>
    </plugin>
  </plugins>
</build>
```
