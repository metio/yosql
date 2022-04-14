---
title: sqlFilesCharset
date: 2022-04-14
menu:
  main:
    parent: Files
categories:
  - Configuration
tags:
  - files
---

The charset to use while reading .sql files.

## Configuration Options

### Option: 'UTF-8'

The default value of the `sqlFilesCharset` configuration option is `UTF-8` which should work on most systems.

### Option: 'ISO-8859-1'

Changing the `sqlFilesCharset` configuration option to `ISO-8859-1` configures `YoSQL` to use the `ISO-8859-1` charset while reading your `.sql` files.

## Related Options

- [inputBaseDirectory](../inputbasedirectory/): The input directory for the user written SQL files.
- [outputBaseDirectory](../outputbasedirectory/): The output directory for the generated classes.
- [skipLines](../skiplines/): The number of lines to skip in each file (e.g. a copyright header).
- [sqlFilesSuffix](../sqlfilessuffix/): The file ending to use while searching for SQL files.
- [sqlStatementSeparator](../sqlstatementseparator/): The separator to split SQL statements inside a single .sql file.

## Tooling

### Ant

In order to use `YoSQL` together with [Ant](https://ant.apache.org/), take a look at the tooling [documentation for Ant](/tooling/ant/).

### Bazel

In order to use `YoSQL` together with [Bazel](https://bazel.build/), take a look at the tooling [documentation for Bazel](/tooling/bazel/).

### CLI

In order to use `YoSQL` on the command line, take a look at the tooling [documentation for CLI](/tooling/cli/).

```console
$ yosql --files-sql-files-charset=configValue
```

As long as the name of the config option is unique across all configuration groups, you can use the shorter form:

```console
$ yosql --sql-files-charset=configValue
```

### Gradle

In order to use `YoSQL` together with [Gradle](https://gradle.org/), take a look at the tooling [documentation for Gradle](/tooling/gradle/). The `sqlFilesCharset` setting can be configured using Gradle in Kotlin syntax like this:

```kotlin
plugins {
  java
  id("wtf.metio.yosql") version "0.0.0-SNAPSHOT"
}

yosql {
  files {
    sqlFilesCharset.set(configValue)
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
    sqlFilesCharset = configValue
  }
}
```

### Maven

In order to use `YoSQL` together with [Maven](https://maven.apache.org/), take a look at the tooling [documentation for Maven](/tooling/maven/). The `sqlFilesCharset` setting can be configured using Maven like this:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>wtf.metio.yosql</groupId>
      <artifactId>yosql-tooling-maven</artifactId>
      <version>0.0.0-SNAPSHOT</version>
      <configuration>
        <files>
          <sqlFilesCharset>configValue</sqlFilesCharset>
        </files>
      </configuration>
    </plugin>
  </plugins>
</build>
```
